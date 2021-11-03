-- liquibase formatted sql

-- changeset Lang:p-amount-spec-1
-- 物料价目表表：P_AMOUNT_SPEC
DROP TABLE IF EXISTS P_AMOUNT_SPEC;
CREATE TABLE IF NOT EXISTS P_AMOUNT_SPEC
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 物料价目表主键',
    `SERIAL`         VARCHAR(255) COMMENT '「serial」- 物料价目表号（系统可用，直接计算）',

    -- 出库商品明细
    `COMMODITY_ID`   VARCHAR(36) COMMENT '「commodityId」- 商品ID',
    `COMMODITY_CODE` VARCHAR(255) COMMENT '「commodityCode」- 商品编码',
    `COMMODITY_NAME` VARCHAR(255) COMMENT '「commodityName」- 商品名称',
    `WH_ID`          VARCHAR(36) COMMENT '「whId」- 所属仓库',
    `AMOUNT_MIN`     DECIMAL(18, 2) COMMENT '「amountMin」- 定价最低',
    `AMOUNT_MAX`     DECIMAL(18, 2) COMMENT '「amountMax」- 定价最高',
    -- 出库单明细相关

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-amount-spec-2
ALTER TABLE P_AMOUNT_SPEC
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 单号不重复