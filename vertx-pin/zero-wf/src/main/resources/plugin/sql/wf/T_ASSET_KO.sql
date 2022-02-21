-- liquibase formatted sql

-- changeset Lang:t-asset-ko-1
-- 资产报废单
DROP TABLE IF EXISTS T_ASSET_KO;
CREATE TABLE IF NOT EXISTS T_ASSET_KO
(
    -- 单据主键
    `KEY`        VARCHAR(36) COMMENT '「key」- 单据主键（主单主键Join模式）',
    -- 从主单中拷贝的可独立成单的业务信息
    `COMMENT_KO` LONGTEXT COMMENT '「commentKo」- 报废原因',
    PRIMARY KEY (`KEY`)
)