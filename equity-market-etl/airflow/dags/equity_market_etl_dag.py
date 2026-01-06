from datetime import datetime, timedelta
import os
import logging

from airflow import DAG
from airflow.operators.python import PythonOperator

# Import pipeline modules
from src.ingestion.ingest_companies import ingest_companies_to_bronze
from src.ingestion.ingest_prices import ingest_prices_to_bronze
from src.validation.data_quality_checks import bronze_checks, silver_checks, gold_checks
from src.transformations.bronze_to_silver import (
    transform_companies_bronze_to_silver,
    transform_prices_bronze_to_silver,
)
from src.transformations.silver_to_gold import (
    create_gold_enriched,
    create_gold_analytics,
)

logger = logging.getLogger(__name__)

ALERT_EMAILS = os.getenv("ALERT_EMAILS", "")  # comma-separated
ALERT_EMAIL_LIST = [e.strip() for e in ALERT_EMAILS.split(",") if e.strip()]

def notify_failure(context):
    """
    Simple failure callback (works everywhere).
    For production: you might integrate Slack/Webhook/PagerDuty.
    """
    dag_id = context.get("dag").dag_id
    task_id = context.get("task_instance").task_id
    run_id = context.get("run_id")
    exc = context.get("exception")
    logger.error(f"[ALERT] DAG={dag_id} TASK={task_id} RUN={run_id} ERROR={exc}")

default_args = { #Belong to Python Operators, we can add mannually to each operator or add here once
    "owner": "data-eng",
    "depends_on_past": False,
    "retries": 3,
    "retry_delay": timedelta(minutes=5),
    "email_on_failure": True if ALERT_EMAIL_LIST else False,
    "email_on_retry": False,
    "email": ALERT_EMAIL_LIST,
    "on_failure_callback": notify_failure,
}

with DAG(
    dag_id="equity_market_etl",
    default_args=default_args,
    description="Batch ETL for equity market data (bronze/silver/gold) with quality checks",
    start_date=datetime(2025, 1, 1),
    schedule_interval="@daily",
    catchup=False,
    max_active_runs=1,
    tags=["market-data", "batch", "medallion"],
) as dag:

    t_ingest_companies = PythonOperator(
        task_id="ingest_companies_to_bronze",
        python_callable=ingest_companies_to_bronze,
    )

    t_ingest_prices = PythonOperator(
        task_id="ingest_prices_to_bronze",
        python_callable=ingest_prices_to_bronze,
    )

    t_bronze_checks = PythonOperator(
        task_id="bronze_data_quality_checks",
        python_callable=bronze_checks,
    )

    t_companies_silver = PythonOperator(
        task_id="transform_companies_bronze_to_silver",
        python_callable=transform_companies_bronze_to_silver,
    )

    t_prices_silver = PythonOperator(
        task_id="transform_prices_bronze_to_silver",
        python_callable=transform_prices_bronze_to_silver,
    )

    t_silver_checks = PythonOperator(
        task_id="silver_data_quality_checks",
        python_callable=silver_checks,
    )

    t_gold_enriched = PythonOperator(
        task_id="create_gold_prices_enriched",
        python_callable=create_gold_enriched,
    )

    t_gold_analytics = PythonOperator(
        task_id="create_gold_analytics",
        python_callable=create_gold_analytics,
    )

    t_gold_checks = PythonOperator(
        task_id="gold_data_quality_checks",
        python_callable=gold_checks,
    )

    # Dependencies
    # 1) Ingest both datasets to bronze
    [t_ingest_companies, t_ingest_prices] >> t_bronze_checks

    # 2) Bronze -> Silver transforms
    t_bronze_checks >> [t_companies_silver, t_prices_silver] >> t_silver_checks

    # 3) Silver -> Gold + Analytics
    t_silver_checks >> t_gold_enriched >> t_gold_analytics >> t_gold_checks
