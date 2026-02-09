-- 查看所有科研项目的详细信息
SELECT id, title, professor_id, start_date, status FROM research_project ORDER BY id;

-- 查看所有教学课程的详细信息
SELECT id, course_name, professor_id, semester, year FROM teaching_course ORDER BY id;
