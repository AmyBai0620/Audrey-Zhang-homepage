-- 检查数据库中的数据
-- 请在PostgreSQL中运行这些查询

-- 1. 查看所有教授
SELECT * FROM professor;

-- 2. 查看白世秀的科研项目
SELECT rp.* FROM research_project rp
JOIN professor p ON rp.professor_id = p.id
WHERE p.name = '白世秀';

-- 3. 查看章金霞的教学课程
SELECT tc.* FROM teaching_course tc
JOIN professor p ON tc.professor_id = p.id
WHERE p.name = '章金霞';

-- 4. 查看所有科研项目（包括professor_id）
SELECT id, title, professor_id, start_date, status FROM research_project;

-- 5. 查看所有教学课程（包括professor_id）
SELECT id, course_name, professor_id, semester, year FROM teaching_course;
