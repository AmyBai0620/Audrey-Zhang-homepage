-- 为 contact_info 表添加地图地址字段
-- 用于存储从地图API获取的详细地址

-- 添加 map_address 字段
ALTER TABLE contact_info
ADD COLUMN map_address VARCHAR(500);

-- 添加字段注释
COMMENT ON COLUMN contact_info.map_address IS '地图详细地址（从地图API自动获取）';

-- 查看表结构确认
SELECT column_name, data_type, character_maximum_length, is_nullable
FROM information_schema.columns
WHERE table_name = 'contact_info'
ORDER BY ordinal_position;
