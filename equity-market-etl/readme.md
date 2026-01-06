# Equity Market ETL Pipeline (Airflow + Spark + Docker)

This project implements an end-to-end ETL pipeline for equity market data using **Apache Airflow**, **Apache Spark**, and **Docker**.  
It is designed as a **local, production-style data engineering environment** to demonstrate orchestration, distributed processing, and containerized workflows.

---

## Project Architecture

- **Airflow**
  - Orchestrates the ETL workflow (DAGs)
  - Uses LocalExecutor
  - Metadata stored in PostgreSQL

- **Spark (Standalone Cluster)**
  - Spark Master + Spark Worker (Docker containers)
  - Executes distributed ETL jobs

- **Docker & Docker Compose**
  - Provides reproducible, isolated runtime
  - All services run inside Docker containers

- **WSL2 Backend (Windows)**
  - Docker runs on top of WSL2
  - Resource limits are managed via `.wslconfig`

---

## Prerequisites

- Docker Desktop (with WSL2 enabled)
- Docker Compose
- Windows 10/11 (WSL2 backend)
- Kaggle account (for datasets)

---

## Datasets

The following datasets are used in this project:

- **All US Stocks (Tickers, Company Info, Logos)**  
  https://www.kaggle.com/datasets/marketahead/all-us-stocks-tickers-company-info-logos

- **S&P 500 with Dividends and Splits (Daily)**  
  https://www.kaggle.com/datasets/benjaminpo/s-and-p-500-with-dividends-and-splits-daily-updated

Download the datasets manually and place them into the appropriate data directory as defined in the project.

---

## Project Startup Guide

### Step 1: Start all services

```bash
docker compose up
# or run in detached mode:
docker compose up -d
# If you have modified the Dockerfile, dependencies, or build configuration, use:
docker compose up --build
```

### Step 2: Initialize the Airflow metadata database
```bash
docker compose exec airflow-webserver airflow db init
```

### Step 3: Create an Airflow admin user
```bash
docker compose exec airflow-webserver airflow users create \
  --username admin \
  --firstname Admin \
  --lastname User \
  --role Admin \
  --email admin@example.com \
  --password admin
```

After this step, you can log in to the Airflow UI & Spark Master UI at:
```
http://localhost:8080
http://localhost:8081
```

### Stopping the Project
When you no longer need to run the services:
```bash
docker up down
```

Cleaning Docker Resources (Optional but Recommended)

Full cleanup (images, containers, volumes)
```bash
docker system prune -a --volumes -f
```
⚠️ Warning: This removes all unused Docker resources globally.

### Docker Disk Usage (WSL2)

Docker on Windows (WSL2 backend) stores all data inside a virtual disk file:
```
C:\Users\<YourUserName>\AppData\Local\Docker\wsl\data\ext4.vhdx
```
This file behaves like a balloon:
 1. It grows as images, containers, and data (e.g., Parquet files) are added
 2. It does not automatically shrink after data deletion

### Reclaim Disk Space
 1. Open Docker Desktop
 2. Click the bug icon (Troubleshoot) in the top-right corner
 3. Select Clean / Purge data
 4. Check WSL 2
 5. Click Delete

### Notes on Performance

Running Spark in Docker + WSL2 introduces resource constraints

CPU and memory limits are controlled via Windows .wslconfig

Compared to local[*] Spark mode, this setup is slower but closer to real production environments

This project prioritizes engineering correctness and architecture clarity over raw performance

### Summary

This project demonstrates:

Workflow orchestration with Airflow

Distributed data processing with Spark

Containerized data engineering environments

Practical handling of resource constraints on local machines

It is intended for learning, experimentation, and showcasing data engineering skills rather than high-throughput production workloads.