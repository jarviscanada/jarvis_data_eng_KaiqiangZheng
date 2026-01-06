from pyspark.sql.functions import col, to_date, trim, regexp_replace
from pyspark.sql.types import DoubleType, LongType
from src.utils.spark_session import get_spark
from src.utils.config import Paths

def transform_companies_bronze_to_silver() -> None:
    p = Paths()
    spark = get_spark()

    df = spark.read.parquet(p.BRONZE_COMPANIES_DIR)

    # Normalize column names manually (because your companies CSV has spaces)
    # We’ll select + alias the ones we need for joins/analytics.
    out = (
        df.select(
            col("ticker").alias("ticker"),
            col("company name").alias("company_name"),
            col("short name").alias("short_name"),
            col("industry").alias("industry"),
            col("sector").alias("sector"),
            col("exchange").alias("exchange"),
            col("market cap").alias("market_cap"),
            col("website").alias("website")
        )
        .withColumn("ticker", trim(col("ticker")))
        .withColumn("company_name", trim(col("company_name")))
        .withColumn("sector", trim(col("sector")))
        .withColumn("industry", trim(col("industry")))
        # Convert market cap to numeric if it’s in scientific notation or string
        .withColumn("market_cap", regexp_replace(col("market_cap"), ",", "").cast(DoubleType()))
        .dropDuplicates(["ticker"])
    )

    out.write.mode("overwrite").parquet(p.SILVER_COMPANIES_DIR)
    spark.stop()


# def transform_prices_bronze_to_silver() -> None:
#     p = Paths()
#     spark = get_spark()

#     df = spark.read.parquet(p.BRONZE_PRICES_DIR)

#     out = (
#         df.select(
#             to_date(col("Date")).alias("date"),
#             col("Open").cast(DoubleType()).alias("open"),
#             col("High").cast(DoubleType()).alias("high"),
#             col("Low").cast(DoubleType()).alias("low"),
#             col("Close").cast(DoubleType()).alias("close"),
#             col("Adj Close").cast(DoubleType()).alias("adj_close"),
#             col("Volume").cast(LongType()).alias("volume"),
#             col("ticker").alias("ticker")
#         )
#         .dropna(subset=["date", "ticker"])
#         .dropDuplicates(["ticker", "date"])
#     )

#     out.write.mode("overwrite").partitionBy("ticker").parquet(p.SILVER_PRICES_DIR)
#     spark.stop()

def transform_prices_bronze_to_silver() -> None:
    p = Paths()
    spark = get_spark()

    # Read the bronze data (raw price data) from parquet format
    df = spark.read.parquet(p.BRONZE_PRICES_DIR)

    out = (
        df.select(
            to_date(col("Date")).alias("date"),  # Convert "Date" column to date type
            col("Open").cast(DoubleType()).alias("open"),  # Cast "Open" to double
            col("High").cast(DoubleType()).alias("high"),  # Cast "High" to double
            col("Low").cast(DoubleType()).alias("low"),  # Cast "Low" to double
            col("Close").cast(DoubleType()).alias("close"),  # Cast "Close" to double
            col("Adj Close").cast(DoubleType()).alias("adj_close"),  # Cast "Adj Close" to double
            col("Volume").cast(LongType()).alias("volume"),  # Cast "Volume" to long type
            col("ticker").alias("ticker")  # Keep the "ticker" column as is
        )
        .dropna(subset=["date", "ticker"])  # Drop rows where "date" or "ticker" are null
        # --- New filtering logic ---
        # Keep rows only when all price-related fields are greater than or equal to 0
        .filter(
            (col("open") >= 0) & 
            (col("high") >= 0) & 
            (col("low") >= 0) & 
            (col("close") >= 0) & 
            (col("adj_close") >= 0) &
            (col("volume") >= 0)
        )
        # Also apply filtering for High >= Low
        .filter(col("high") >= col("low"))
        # -------------------
        .dropDuplicates(["ticker", "date"])  # Remove duplicates based on ticker and date
    )

    # Write the cleaned data into silver layer (partitioned by ticker)
    out.write.mode("overwrite").partitionBy("ticker").parquet(p.SILVER_PRICES_DIR)

    # (
    # out.repartition("ticker")  # Key: Let Spark first group the data by 'ticker'
    # .write
    # .mode("overwrite") 
    # .partitionBy("ticker")  
    # .parquet(p.SILVER_PRICES_DIR)
    # )
    spark.stop()  # Stop the Spark session
