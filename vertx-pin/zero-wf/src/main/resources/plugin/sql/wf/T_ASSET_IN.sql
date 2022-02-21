-- liquibase formatted sql

-- changeset Lang:t-asset-in-1
-- 资产入库单
DROP TABLE IF EXISTS T_ASSET_IN;
CREATE TABLE IF NOT EXISTS T_ASSET_IN
(
    -- 单据主键
    `KEY`        VARCHAR(36) COMMENT '「key」- 单据主键（主单主键Join模式）',
    -- 从主单中拷贝的可独立成单的业务信息
    `COMMENT_IN` LONGTEXT COMMENT '「commentIn」- 入库备注',
    PRIMARY KEY (`KEY`)
)