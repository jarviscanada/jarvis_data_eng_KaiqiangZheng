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

# save hostname and cpu_info as variables
lscpu_out=$(lscpu)
hostname=$(hostname -f)

# Retrieve hardware specification variables
# xargs is a trick to trim leading and trailing white spaces
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | cut -d ':' -f2 | xargs)
cpu_mhz=$(cat /proc/cpuinfo | grep "cpu MHz" | awk '{print$4}' | uniq)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K//')
total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2 / 1024}' | xargs) # kb/1024 to mb,I think below is memory free, not total_memory
# total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')

# Current time in `2025-10-21 15:13:51` UTC format
timestamp=$(vmstat -t | awk '{print $(NF-1), $NF}' | tail -n1 | xargs)


# PSQL command: Inserts server usage data into host_info table
# Note: be careful with double and single quotes
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem) \
VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"

# Print the SQL statement being executed
echo "Running SQL: $insert_stmt"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

# Capture exit status in a variable immediately
psql_exit_status=$?

# Check the exit status of the previous command
if [ $psql_exit_status -eq 0 ]; then
    echo "host_info inserted successfully."
else
    echo "Failed to insert host_usage data."
fi

exit $psql_exit_status  #exit $?