from pyspark.sql import SparkSession
from src.utils.config import PipelineConfig

def get_spark() -> SparkSession:
    """
    Create and configure a Spark session based on the PipelineConfig settings.
    
    Returns:
        SparkSession: Configured Spark session.
    """
    cfg = PipelineConfig()
    
    spark = (
        SparkSession.builder
        .appName(cfg.APP_NAME)
        .master(cfg.SPARK_MASTER)
        .config("spark.sql.shuffle.partitions", cfg.SHUFFLE_PARTITIONS)
        .getOrCreate()
    )
    spark.sparkContext.setLogLevel("WARN")

    return spark