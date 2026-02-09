-- 清理错误的测试数据
-- 删除那些title为空的科研项目记录
DELETE FROM research_project WHERE title IS NULL OR title = '';

-- 删除那些course_name为空的教学课程记录
DELETE FROM teaching_course WHERE course_name IS NULL OR course_name = '';

-- 查看清理后的结果
SELECT 'Research Projects' as table_name, COUNT(*) as count FROM research_project
UNION ALL
SELECT 'Teaching Courses', COUNT(*) FROM teaching_course;
