-- 查看章金霞（ID=2）的所有数据
SELECT 'Publications' as type, COUNT(*) as count FROM publication WHERE professor_id = 2
UNION ALL
SELECT 'Educations', COUNT(*) FROM education WHERE professor_id = 2
UNION ALL
SELECT 'Research Projects', COUNT(*) FROM research_project WHERE professor_id = 2
UNION ALL
SELECT 'Teaching Courses', COUNT(*) FROM teaching_course WHERE professor_id = 2
UNION ALL
SELECT 'Awards', COUNT(*) FROM award WHERE professor_id = 2;

-- 查看章金霞的教学课程详情
SELECT * FROM teaching_course WHERE professor_id = 2;
