<!-- # Linux Cluster Monitoring Agent
This project is under development. Since this project follows the GitFlow, the final work will be merged to the main branch after Team Code Team. -->

# Linux Cluster Monitoring Agent

## 🧭 Introduction
This project implements a **Linux Cluster Monitoring Agent** that collects and stores hardware specifications and real-time resource usage data from multiple Linux servers into a centralized PostgreSQL database.  
The tool allows system administrators and DevOps teams to monitor CPU, memory, and disk usage across servers in a distributed environment.  

The project is designed for automation and scalability: hardware data are collected once per host, while usage metrics are collected every minute using **crontab**.  
Key technologies include **Bash scripting**, **PostgreSQL**, **Docker**, and **Git**, following DevOps-style version control and automation practices.

---

## ⚡ Quick Start
```bash
# 1️⃣ Start a PostgreSQL instance using Docker
bash scripts/psql_docker.sh create postgres password
bash scripts/psql_docker.sh start  (#Not necessary as it will start automatically after I run it)

# 2️⃣ Create the database and tables
-- connect to the psql instance
psql -h localhost -U postgres -W
-- create a database
postgres=# CREATE DATABASE host_agent;

psql -h localhost -p 5432 -U postgres -d host_agent -f sql/ddl.sql

# 3️⃣ Insert host hardware specification (run once)
bash scripts/host_info.sh localhost 5432 host_agent postgres password

# 4️⃣ Insert real-time host usage data (run once to test)
bash scripts/host_usage.sh localhost 5432 host_agent postgres password

# 5️⃣ Automate usage collection every minute using crontab
crontab -e
* * * * * bash /home/rocky/dev/jarvis_data_eng_KaiqiangZheng/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log 2>&1
```

## Implemenation
The system consists of multiple Linux hosts connected to a single PostgreSQL database running in a Docker container. Each host runs two Bash scripts:

host_info.sh – Collects static hardware info (CPU, memory, etc.)

host_usage.sh – Collects live resource usage metrics every minute

The data is sent to the centralized database, enabling query-based performance tracking and trend analysis.

### Architecture
Cluster Overview:

3 Linux Hosts: run host_info.sh and host_usage.sh scripts

1 PostgreSQL DB (Docker): stores host information and usage data

Crontab Scheduler: automates periodic data collection

📊 A visual diagram (created using draw.io) is located in the /assets directory as architecture.png.
(assets/cluster_diagram.png)

![Architecture Diagram](assets/cluster_diagram.png)

### Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh
```bash
  - bash scripts/psql_docker.sh create postgres password
  - bash scripts/psql_docker.sh start
  - bash scripts/psql_docker.sh stop
```
- host_info.sh
```bash
  - bash scripts/host_info.sh localhost 5432 host_agent postgres password
```
- host_usage.sh
```bash
  - bash scripts/host_usage.sh localhost 5432 host_agent postgres password
```
- crontab
```bash
  - * * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log 2>&1
```
- queries.sql (describe what business problem you are trying to resolve)
  - Purpose: Example analytical queries for resource utilization.
    - Identify hosts with the lowest memory.
    - Compute average CPU idle time.
    - Monitor disk usage trends.


### Database Modeling
Describe the schema of each table using markdown table syntax (do not put any sql code)
- `host_info`

| Column Name      | Data Type   | Description              |
| ---------------- | ----------- | ------------------------ |
| id               | SERIAL (PK) | Unique host identifier   |
| hostname         | VARCHAR     | Name of the host machine |
| cpu_number       | INT         | Number of CPU cores      |
| cpu_architecture | VARCHAR     | CPU architecture type    |
| cpu_model        | VARCHAR     | CPU model name           |
| total_mem        | BIGINT      | Total memory in kB       |
| timestamp        | TIMESTAMP   | Data collection time     |

- `host_usage`

| Column Name    | Data Type   | Description                         |
| -------------- | ----------- | ----------------------------------- |
| timestamp      | TIMESTAMP   | Time of record collection           |
| host_id        | SERIAL (FK) | Foreign key referencing `host_info` |
| memory_free    | BIGINT      | Available memory in kB              |
| cpu_idle       | INT         | CPU idle percentage                 |
| cpu_kernel     | INT         | CPU kernel percentage               |
| disk_io        | INT         | Disk I/O utilization                |
| disk_available | BIGINT      | Available disk space in MB          |


## Test
How did you test your bash scripts DDL? What was the result

1. Verify PostgreSQL container is running
```bash
docker ps
```
3. Check tables
```bash
psql -h localhost -U postgres -d host_agent -c "\dt"
```
4. Validate inserted data
```bash
psql -h localhost -U postgres -d host_agent -c "SELECT * FROM host_info;"
psql -h localhost -U postgres -d host_agent -c "SELECT * FROM host_usage LIMIT 5;"
```
## Deployment
How did you deploy your app? (e.g. Github, crontab, docker)

1. Clone repository
```bash
git clone https://github.com/jarviscanada/jarvis_data_eng_KaiqiangZheng.git 
(#Or git@github.com:jarviscanada/jarvis_data_eng_KaiqiangZheng.git)

cd linux_sql/host_agent/scripts
```
2. Start PostgreSQL container
```bash
bash psql_docker.sh create postgres password
```
-- connect to the psql instance
```bash
psql -h localhost -U postgres -W
```
-- create a database
```bash
postgres=# CREATE DATABASE host_agent;
```

3. Create database and tables
```bash
psql -h localhost -U postgres -f sql/ddl.sql
```

4. Insert host info
```bash
bash host_info.sh localhost 5432 host_agent postgres password
```

5. Set up crontab for automated data collection
```bash
crontab -e
* * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log 2>&1
```

## Improvements
Write at least three things you want to improve 
e.g. 
1. Dynamic Host Update:
  - Automatically detect and update hardware configuration changes (e.g., CPU upgrades).
2. Monitoring Dashboard:
  - Integrate a visual dashboard (Grafana or Flask web app) for real-time monitoring.
3. Error Handling & Logging:
  - Add error handling and retry logic in Bash scripts to recover from DB connection issues.
