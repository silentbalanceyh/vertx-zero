-- liquibase formatted sql

-- changeset Lang:t-asset-out-1
-- 资产出库单
DROP TABLE IF EXISTS T_ASSET_OUT;
CREATE TABLE IF NOT EXISTS T_ASSET_OUT
(
    -- 单据主键
    `KEY`           VARCHAR(36) COMMENT '「key」- 单据主键（主单主键Join模式）',
    -- 从主单中拷贝的可独立成单的业务信息
    `COMMENT_OUT`   LONGTEXT COMMENT '「commentOut」- 出库备注',
    `COMMENT_USAGE` LONGTEXT COMMENT '「commentUsage」- 出库使用说明',
    PRIMARY KEY (`KEY`)
)