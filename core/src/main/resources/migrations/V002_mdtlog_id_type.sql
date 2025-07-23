-- 주의: 데이터 날아감
-- TRUNCATE mdt_log;

ALTER TABLE mdt_log MODIFY COLUMN log_id BIGINT NOT NULL;
