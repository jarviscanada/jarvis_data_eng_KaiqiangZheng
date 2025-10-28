--Optional Tickets
--1.
SELECT cpu_number, id, total_mem
FROM host_info
ORDER BY cpu_number, total_mem DESC;

--2.
CREATE OR REPLACE FUNCTION round5(ts TIMESTAMP)
RETURNS TIMESTAMP AS $$
BEGIN
RETURN date_trunc('hour', ts) + (date_part('minute', ts)::int / 5) * INTERVAL '5 min';
END;
$$ LANGUAGE plpgsql;

SELECT
    hu.host_id,
    hi.hostname,
    round5(hu."timestamp") AS ts_5min,
    ROUND(AVG((hi.total_mem - hu.memory_free) * 100.0 / hi.total_mem)) AS avg_used_mem_percentage
FROM host_usage hu
         JOIN host_info hi ON hu.host_id = hi.id
GROUP BY hu.host_id, hi.hostname, round5(hu."timestamp")
ORDER BY hu.host_id, ts_5min;

--3.
SELECT
    hu.host_id,
    hi.hostname,
    round5(hu."timestamp") AS ts_5min,
    COUNT(*) AS num_data_points
FROM host_usage hu
         JOIN host_info hi ON hu.host_id = hi.id
GROUP BY hu.host_id, hi.hostname, round5(hu."timestamp")
HAVING COUNT(*) < 3
ORDER BY hu.host_id, ts_5min;
