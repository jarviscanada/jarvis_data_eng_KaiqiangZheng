--Modifying Data
--Q1
INSERT INTO cd.facilities
VALUES (9,'Spa',20,30,100000,800);

--Q2
--Modifying Data
--Q1
INSERT INTO cd.facilities
VALUES (9,'Spa',20,30,100000,800);

--Q2
INSERT INTO cd.facilities
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

--DELETE FROM cd.facilities WHERE facid = 10;

--Q3
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

--Q4
UPDATE cd.facilities
SET
    membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
    guestcost = (select guestcost * 1.1 from cd.facilities where facid = 0)
WHERE facid = 1;

--Q5
DELETE FROM cd.bookings;
--OR
TRUNCATE cd.bookings;

--Q6
DELETE FROM cd.members
WHERE memid = 37;

--Basics
--Q7
SELECT facid,name,membercost,monthlymaintenance
FROM cd.facilities
WHERE membercost < (1.0/50)*monthlymaintenance AND membercost > 0;
--or where membercost < monthlymaintenance/50.0 AND membercost <> 0;

--Q8
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

--Q9
SELECT * FROM cd.facilities
WHERE facid IN (1,5);

--Q10
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';

--Q11
SELECT surname FROM cd.members
UNION
SELECT name from cd.facilities;

--Join
--Q12
SELECT bks.starttime FROM cd.bookings bks
INNER JOIN cd.members mems ON bks.memid = mems.memid
WHERE mems.firstname = 'David' AND mems.surname = 'Farrell';

--Q13
SELECT bks.starttime AS start, fas.name
FROM cd.bookings bks
         INNER JOIN cd.facilities fas ON bks.facid = fas.facid
WHERE DATE(bks.starttime) = '2012-09-21'
  AND fas.name LIKE '%Tennis Court%'
ORDER BY bks.starttime;
--or
SELECT bks.starttime AS start, fas.name
FROM cd.bookings bks
         INNER JOIN cd.facilities fas ON bks.facid = fas.facid
WHERE bks.starttime BETWEEN '2012-09-21' AND '2012-09-22'
  AND fas.name LIKE '%Tennis Court%'
ORDER BY bks.starttime;
--or
select bks.starttime as start, facs.name as name
from
    cd.facilities facs
        inner join cd.bookings bks
                   on facs.facid = bks.facid
where
    facs.name in ('Tennis Court 2','Tennis Court 1') and
    bks.starttime >= '2012-09-21' and
    bks.starttime < '2012-09-22'
order by bks.starttime;

--Q14
SELECT mems.firstname AS memfname, mems.surname AS memsname,
       recs.firstname AS recfname, recs.surname AS recsname
FROM cd.members mems
         LEFT JOIN cd.members recs ON mems.recommendedby = recs.memid
ORDER BY memsname,memfname;

--Q15
SELECT DISTINCT recs.firstname, recs.surname
FROM cd.members mems
         LEFT JOIN cd.members recs ON mems.recommendedby = recs.memid
WHERE mems.recommendedby IS NOT NULL
ORDER BY recs.surname, recs.firstname;
--or
SELECT DISTINCT recs.firstname, recs.surname
FROM cd.members mems
         INNER JOIN cd.members recs ON mems.recommendedby = recs.memid
ORDER BY recs.surname, recs.firstname;

--Q16
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname AS member,
    (
        SELECT recs.firstname || ' ' || recs.surname AS recommender
        FROM cd.members recs
        WHERE recs.memid = mems.recommendedby
    )
FROM cd.members mems
ORDER BY member;

--Aggregation
--Q17
SELECT recommendedby, count(recommendedby) FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
--or count(*)
--count(recommendedby): will not count null but null still exists in recommendedby column with empty count

--Q18
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER by facid;

--Q19
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-09-01' AND '2012-10-01'
GROUP BY facid
ORDER BY "Total Slots";

--Q20
SELECT facid, extract(month FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE extract(year FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;

--Q21
SELECT COUNT(DISTINCT memid) FROM cd.bookings;

--Q22
-- We can skip table aliases if there are no column name conflicts between the tables. ONLY m.memid
--In the GROUP BY clause, we do not need to list firstname and surname separately since ID column is unique
SELECT surname, firstname, m.memid, min(starttime)
FROM cd.members m
         JOIN cd.bookings b ON m.memid = b.memid
WHERE starttime >= '2012-09-01'
GROUP BY surname,firstname,m.memid
ORDER BY m.memid;


--Q23
SELECT COUNT(*) over(), firstname, surname
FROM cd.members
ORDER BY joindate;

--Q24
SELECT
    ROW_NUMBER() OVER(ORDER BY joindate),
    firstname,
    surname
FROM cd.members
ORDER BY joindate;

--Q25
SELECT facid, total FROM (
    SELECT facid, sum(slots) total, rank() over (ORDER BY sum(slots) DESC) rank
    FROM cd.bookings
    GROUP BY facid
    ) AS ranked
WHERE rank = 1

--String
--Q26
SELECT surname || ', ' || firstname AS name
FROM cd.members;

--Q27
SELECT memid, telephone
FROM cd.members
WHERE telephone ~ '[()]';
--or
SELECT memid, telephone
FROM cd.members
WHERE telephone SIMILAR TO '%[()]%';
--or
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(___)%';

--Q28
SELECT SUBSTR(mems.surname, 1, 1) AS letter, COUNT(*) AS count
FROM cd.members mems
GROUP BY letter
ORDER BY letter;


