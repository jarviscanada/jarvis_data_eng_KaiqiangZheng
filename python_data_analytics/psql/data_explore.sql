-- Show table schema 
\d+ retail;

-- Q1: Show first 10 rows
SELECT * FROM retail limit 10;
--SELECT * FROM retail order by invoice_no DESC limit 10; To see the last 10


-- Q2: Check # of records
SELECT COUNT(*) AS count
FROM retail;
    

-- Q3: number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) AS "Number of Clients"
FROM retail;


-- Q4: invoice date range (e.g. max/min dates)
SELECT 
    MAX(invoice_date) AS max,
    MIN(invoice_date) AS min
FROM retail;

SELECT (MAX(invoice_date) - MIN(invoice_date)) AS "Time Range"
FROM retail;

    
-- Q5: number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) AS "Number of SKU/Merchants"
FROM retail;


/*- Q6: Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
    - an invoice consists of one or more items where each item is a row in the df
    - hint: you need to use GROUP BY and HAVING */
SELECT AVG(total_amount) AS "Average Invoice Amount"
FROM (
    SELECT invoice_no, SUM(quantity * unit_price) AS total_amount
    FROM retail
    GROUP BY invoice_no
    HAVING SUM(quantity * unit_price) > 0
) AS invoice_totals;

/*SELECT invoice_no, SUM(quantity * unit_price) AS total_amount
FROM retail
GROUP BY invoice_no
HAVING SUM(quantity * unit_price) < 0;*/ -- To see the negative amount

    
-- Q7: Calculate total revenue (e.g. sum of unit_price * quantity)
SELECT SUM(quantity * unit_price) AS "Total Revenue"
FROM retail;


-- Q8: Calculate total revenue by YYYYMM
SELECT
    CAST(EXTRACT(YEAR FROM invoice_date) AS INTEGER) * 100 +
    CAST(EXTRACT(MONTH FROM invoice_date) AS INTEGER) AS yyyymm,
    SUM(quantity * unit_price) AS revenue
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;

--postgreSQL only ::int;  CAST for SQL standard syntax
SELECT 
    (EXTRACT(YEAR FROM invoice_date)::int * 100 
        + EXTRACT(MONTH FROM invoice_date)::int) AS yyyymm,
    SUM(quantity * unit_price) AS sum
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;


