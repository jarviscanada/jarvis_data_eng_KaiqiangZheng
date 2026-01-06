from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime
import os

RAW_PRICE_PATH = "/opt/data/raw/us_stocks_etfs"
RAW_COMPANY_PATH = "/opt/data/raw/company_info_and_logos/companies.csv"


def list_price_files():
    files = os.listdir(RAW_PRICE_PATH)
    print(f"Found {len(files)} price files")
    print(files[:5])


def check_company_file():
    if not os.path.exists(RAW_COMPANY_PATH):
        raise FileNotFoundError("companies.csv not found")
    print("Company file exists")


with DAG(
    dag_id="learning_equity_market_etl",
    start_date=datetime(2024, 1, 1),
    schedule_interval="@daily",
    catchup=False,
) as dag:

    list_files = PythonOperator(
        task_id="list_price_files",
        python_callable=list_price_files,
    )

    check_company = PythonOperator(
        task_id="check_company_file",
        python_callable=check_company_file,
    )

    list_files >> check_company
