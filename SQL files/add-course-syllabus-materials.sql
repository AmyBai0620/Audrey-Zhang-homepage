-- 为 teaching_course 表添加课程大纲和资料字段
-- 用于存储课程的详细大纲和相关学习资料

-- 添加 syllabus 字段（课程大纲，富文本内容）
ALTER TABLE teaching_course
ADD COLUMN syllabus TEXT;

-- 添加 materials 字段（课程资料，JSON格式存储文件列表）
ALTER TABLE teaching_course
ADD COLUMN materials TEXT;

-- 添加字段注释
COMMENT ON COLUMN teaching_course.syllabus IS '课程大纲（富文本HTML内容）';
COMMENT ON COLUMN teaching_course.materials IS '课程资料列表（JSON格式：[{"name":"文件名","url":"文件URL","size":文件大小}]）';

-- 查看表结构确认
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'teaching_course'
ORDER BY ordinal_position;

-- 查询示例数据
SELECT id, course_name, syllabus IS NOT NULL as has_syllabus, materials IS NOT NULL as has_materials
FROM teaching_course
LIMIT 5;
