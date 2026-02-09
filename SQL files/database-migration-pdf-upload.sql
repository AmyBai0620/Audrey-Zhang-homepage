-- ========================================
-- 数据库迁移脚本：添加论文PDF上传功能
-- 执行时间：2026-01-20
-- ========================================

-- 1. 为 publication 表添加 pdf_url 字段
ALTER TABLE publication ADD COLUMN pdf_url VARCHAR(500);

-- 说明：
-- pdf_url 字段用于存储论文PDF文件的路径
-- 示例值：/uploads/papers/1_abc123.pdf
-- VARCHAR(500) 足够存储文件路径

-- 验证：查看表结构
-- \d publication  (PostgreSQL命令)
-- 或者
-- SELECT column_name, data_type, character_maximum_length
-- FROM information_schema.columns
-- WHERE table_name = 'publication' AND column_name = 'pdf_url';
