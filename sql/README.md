# Introduction
This project focuses on learning and applying fundamental and advanced SQL concepts through practical exercises and data modeling tasks. The main objective is to design a relational database schema, define data relationships using DDL (Data Definition Language), and perform CRUD operations, joins, aggregations, and subqueries to gain hands-on experience with PostgreSQL. The project also includes initializing a PostgreSQL instance using Docker, managing data through the DBeaver SQL IDE, and exploring realistic datasets such as the club membership database (clubdata.sql). By the end of this project, the environment simulates a real-world database workflow (from schema design and data loading to analytical querying) strengthening both database design understanding and SQL proficiency.

# SQL Queries

###### Table Setup (DDL)
```sql
CREATE TABLE cd.members
    (
       memid integer NOT NULL, 
       surname character varying(200) NOT NULL, 
       firstname character varying(200) NOT NULL, 
       address character varying(300) NOT NULL, 
       zipcode integer NOT NULL, 
       telephone character varying(20) NOT NULL, 
       recommendedby integer,
       joindate timestamp NOT NULL,
       CONSTRAINT members_pk PRIMARY KEY (memid),
       CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
            REFERENCES cd.members(memid) ON DELETE SET NULL
    );

CREATE TABLE cd.facilities
    (
       facid integer NOT NULL, 
       name character varying(100) NOT NULL, 
       membercost numeric NOT NULL, 
       guestcost numeric NOT NULL, 
       initialoutlay numeric NOT NULL, 
       monthlymaintenance numeric NOT NULL, 
       CONSTRAINT facilities_pk PRIMARY KEY (facid)
    );

CREATE TABLE cd.bookings
    (
       bookid integer NOT NULL, 
       facid integer NOT NULL, 
       memid integer NOT NULL, 
       starttime timestamp NOT NULL,
       slots integer NOT NULL,
       CONSTRAINT bookings_pk PRIMARY KEY (bookid),
       CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
       CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
    );
```

###### Modifying Data
###### Question 1: Insert some data into facilities table

```sql
INSERT INTO cd.facilities
VALUES (9,'Spa',20,30,100000,800);
```

###### Question 2: Insert calculated data into a table

```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;
```

###### Question 3: Update some existing data

```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';
```

###### Question 4: Update a row based on the contents of another row

```sql
UPDATE cd.facilities
SET
    membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
    guestcost = (select guestcost * 1.1 from cd.facilities where facid = 0)
WHERE facid = 1;  
```

###### Question 5: Delete all bookings

```sql
DELETE FROM cd.bookings;
--OR
TRUNCATE cd.bookings;
```

###### Question 6: Delete a member from the cd.members table

```sql
DELETE FROM cd.members
WHERE memid = 37;
```

###### Basics
###### Question 7: Control which rows are retrieved - part 2

```sql
SELECT facid,name,membercost,monthlymaintenance
FROM cd.facilities
WHERE membercost < (1.0/50)*monthlymaintenance AND membercost > 0;
```

###### Question 8: Basic string searches

```sql
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

###### Question 9: Matching against multiple possible values

```sql
SELECT * FROM cd.facilities
WHERE facid IN (1,5);
```

###### Question 10: Working with dates

```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';
```

###### Question 11: Combining results from multiple queries

```sql
SELECT surname FROM cd.members
UNION
SELECT name from cd.facilities;
```

###### Join
###### Question 12: Retrieve the start times of members' bookings

```sql
SELECT bks.starttime FROM cd.bookings bks
INNER JOIN cd.members mems ON bks.memid = mems.memid
WHERE mems.firstname = 'David' AND mems.surname = 'Farrell';
```

###### Question 13: Work out the start times of bookings for tennis courts

```sql
SELECT bks.starttime AS start, fas.name
FROM cd.bookings bks
         INNER JOIN cd.facilities fas ON bks.facid = fas.facid
WHERE bks.starttime BETWEEN '2012-09-21' AND '2012-09-22'
  AND fas.name LIKE '%Tennis Court%'
ORDER BY bks.starttime;
```

###### Question 14: Produce a list of all members, along with their recommender

```sql
SELECT mems.firstname AS memfname, mems.surname AS memsname,
       recs.firstname AS recfname, recs.surname AS recsname
FROM cd.members mems
         LEFT JOIN cd.members recs ON mems.recommendedby = recs.memid
ORDER BY memsname,memfname;
```

###### Question 15: Produce a list of all members who have recommended another member

```sql
SELECT DISTINCT recs.firstname, recs.surname
FROM cd.members mems
         INNER JOIN cd.members recs ON mems.recommendedby = recs.memid
ORDER BY recs.surname, recs.firstname;
```

###### Question 16: Produce a list of all members, along with their recommender, using no joins.

```sql
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname AS member,
    (
        SELECT recs.firstname || ' ' || recs.surname AS recommender
        FROM cd.members recs
        WHERE recs.memid = mems.recommendedby
    )
FROM cd.members mems
ORDER BY member;
```

###### Aggregation
###### Question 17: Count the number of recommendations each member makes.

```sql
SELECT recommendedby, count(recommendedby) FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
```

###### Question 18: List the total slots booked per facility

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER by facid;
```

###### Question 19: List the total slots booked per facility in a given month

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-09-01' AND '2012-10-01'
GROUP BY facid
ORDER BY "Total Slots";
```

###### Question 20: List the total slots booked per facility per month

```sql
SELECT facid, extract(month FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE extract(year FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;
```

###### Question 21: Find the count of members who have made at least one booking

```sql
SELECT COUNT(DISTINCT memid) FROM cd.bookings;
```

###### Question 22: List each member's first booking after September 1st 2012

```sql
SELECT surname, firstname, m.memid, min(starttime)
FROM cd.members m
         JOIN cd.bookings b ON m.memid = b.memid
WHERE starttime >= '2012-09-01'
GROUP BY surname,firstname,m.memid
ORDER BY m.memid;
```

###### Question 23: Produce a list of member names, with each row containing the total member count

```sql
SELECT COUNT(*) over(), firstname, surname
FROM cd.members
ORDER BY joindate;
```

###### Question 24: Produce a numbered list of members

```sql
SELECT
    ROW_NUMBER() OVER(ORDER BY joindate),
    firstname,
    surname
FROM cd.members
ORDER BY joindate;
```

###### Question 25: Output the facility id that has the highest number of slots booked, again

```sql
SELECT facid, total FROM (
    SELECT facid, SUM(slots) total, RANK() over (ORDER BY SUM(slots) DESC) rank
    FROM cd.bookings
    GROUP BY facid
    ) AS ranked
WHERE rank = 1
```

###### String
###### Question 26: Format the names of members

```sql
SELECT surname || ', ' || firstname AS name
FROM cd.members;
```

###### Question 27: Find telephone numbers with parentheses

```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone ~ '[()]';
```

###### Question 28: Count the number of members whose surname starts with each letter of the alphabet

```sql
SELECT SUBSTR(mems.surname, 1, 1) AS letter, COUNT(*) AS count
FROM cd.members mems
GROUP BY letter
ORDER BY letter;
```
