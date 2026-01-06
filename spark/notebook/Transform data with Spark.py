# Databricks notebook source
# MAGIC  %sql
# MAGIC  CREATE VOLUME IF NOT EXISTS spark_lab

# COMMAND ----------

import requests

# Define the current catalog
catalog_name = spark.sql("SELECT current_catalog()").collect()[0][0]

# Define the base path using the current catalog
volume_base = f"/Volumes/{catalog_name}/default/spark_lab"

# List of files to download
files = ["2019.csv", "2020.csv", "2021.csv"]

# Download each file
for file in files:
    url = f"https://raw.githubusercontent.com/MicrosoftLearning/mslearn-databricks/main/data/{file}"
    response = requests.get(url)
    response.raise_for_status()

    # Write to Unity Catalog volume
    with open(f"{volume_base}/{file}", "wb") as f:
        f.write(response.content)

# COMMAND ----------

spark.sql("SELECT current_catalog()").show()
display(spark.sql("SELECT current_catalog()").collect()[0])
print('\n')
display(spark.sql("SELECT current_catalog()").collect()[0][0])

# COMMAND ----------

from pyspark.sql.types import *
from pyspark.sql.functions import *
orderSchema = StructType([
     StructField("SalesOrderNumber", StringType()),
     StructField("SalesOrderLineNumber", IntegerType()),
     StructField("OrderDate", DateType()),
     StructField("CustomerName", StringType()),
     StructField("Email", StringType()),
     StructField("Item", StringType()),
     StructField("Quantity", IntegerType()),
     StructField("UnitPrice", FloatType()),
     StructField("Tax", FloatType())
])
df = spark.read.load(f'/Volumes/{catalog_name}/default/spark_lab/*.csv', format='csv', schema=orderSchema)
display(df.limit(100))

# COMMAND ----------

from pyspark.sql.functions import col
df = df.dropDuplicates()
df = df.withColumn('Tax', col('UnitPrice') * 0.08)
df = df.withColumn('Tax', col('Tax').cast("float"))
display(df.limit(100))

# COMMAND ----------

customers = df['CustomerName', 'Email']
print(customers.count())
print(customers.distinct().count())
display(customers.distinct())

# COMMAND ----------

customers = df.select("CustomerName", "Email").where(df['Item']=='Road-250 Red, 52')
print(customers.count())
print(customers.distinct().count())
display(customers.distinct())

# COMMAND ----------

productSales = df.select("Item", "Quantity").groupBy("Item").sum()
display(productSales)

# COMMAND ----------

yearSales = df.select(year("OrderDate").alias("Year")).groupBy("Year").count().orderBy("Year")
display(yearSales)

# COMMAND ----------

df.createOrReplaceTempView("salesorders")

# COMMAND ----------

# MAGIC %sql
# MAGIC     
# MAGIC SELECT YEAR(OrderDate) AS OrderYear,
# MAGIC        SUM((UnitPrice * Quantity) + Tax) AS GrossRevenue
# MAGIC FROM salesorders
# MAGIC GROUP BY YEAR(OrderDate)
# MAGIC ORDER BY OrderYear;