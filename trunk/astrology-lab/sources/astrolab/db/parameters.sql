## jdbc:mysql://localhost:3306/astrolab?useUnicode=true&connectionCollation=cp1251_bulgarian_ci&characterEncoding=cp1251


SELECT DISTINCT(text.id) as app_id, TABLE1.app1, TABLE2.app2, TABLE11.app11, TABLE21.app21
 FROM text

JOIN

(SELECT text.id as app1_id, COUNT(approved.user_id) as app1
 FROM text
 LEFT JOIN (SELECT * from help_feedback WHERE approve = 'yes') as approved
 ON text.id = approved.id
GROUP BY text.id)
AS TABLE1
ON text.id = TABLE1.app1_id

JOIN

(SELECT text.id as app11_id, COUNT(has_approved.user_id) as app11
 FROM text
 LEFT JOIN (SELECT * from help_feedback WHERE approve = 'yes' AND user_id = 2000006) as has_approved
 ON text.id = has_approved.id
GROUP BY text.id)
AS TABLE11
ON text.id = TABLE11.app11_id

JOIN 

(SELECT text.id as app2_id, COUNT(disapproved.user_id) as app2
 FROM text
 LEFT JOIN (SELECT * from help_feedback WHERE approve = 'no') as disapproved
 ON text.id = disapproved.id
GROUP BY text.id)
AS TABLE2
ON text.id = TABLE2.app2_id

JOIN

(SELECT text.id as app21_id, COUNT(has_disapproved.user_id) as app21
 FROM text
 LEFT JOIN (SELECT * from help_feedback WHERE approve = 'no' AND user_id = 2000006) as has_disapproved
 ON text.id = has_disapproved.id
GROUP BY text.id)
AS TABLE21
ON text.id = TABLE21.app21_id

WHERE TABLE1.app1 + TABLE2.app2 > 0