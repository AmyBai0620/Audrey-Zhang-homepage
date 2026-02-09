-- 图片上传功能 - 数据库变更脚本
-- 执行日期：2026-01-20

-- 1. 教授表添加头像字段
ALTER TABLE professor ADD COLUMN avatar_url VARCHAR(500);

-- 2. 科研项目表添加图片字段（JSON数组格式）
ALTER TABLE research_project ADD COLUMN images TEXT;

-- 验证变更
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_name = 'professor' AND column_name = 'avatar_url';

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'research_project' AND column_name = 'images';
