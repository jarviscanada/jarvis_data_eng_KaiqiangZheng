#!/bin/bash

# Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Retrieve hardware specification variables
# xargs is a trick to trim leading and trailing white spaces
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}') #...
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}') #...
disk_io=$(vmstat -d | tail -1 | awk '{print $10}') #..
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | sed 's/M//') #...

# Current time in `2025-10-21 10:50:31` UTC format
timestamp=$(vmstat -t | awk '{print $(NF-1), $NF}' | tail -n1 | xargs) #...

# Subquery to find matching id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

# PSQL command: Inserts server usage data into host_usage table
# Note: be careful with double and single quotes
#insert_stmt="INSERT INTO host_usage(timestamp, ...) VALUES('$timestamp', #....

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) \
VALUES ('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

# Print the SQL statement being executed
echo "Running SQL: $insert_stmt"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
#exit $?
# Capture exit status in a variable immediately
psql_exit_status=$?

# Check the exit status of the previous command
if [ $psql_exit_status -eq 0 ]; then
    echo "host_usage inserted successfully."
else
    echo "Failed to insert host_usage data."
fi

exit $psql_exit_status