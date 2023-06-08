-- liquibase formatted sql

-- changeset Lang:p-commodity-his-1
-- 库存变更记录：P_COMMODITY_HIS
DROP TABLE IF EXISTS P_COMMODITY_HIS;
CREATE TABLE IF NOT EXISTS P_COMMODITY_HIS
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 产品变更记录主键',
    `CODE`         VARCHAR(255) COMMENT '「code」- 产品变更编号（系统可用）',
    `TYPE`         VARCHAR(36) COMMENT '「type」- 产品变更类型：IN/OUT，出入',
    `COMMODITY_ID` VARCHAR(36) COMMENT '「commodityId」- 产品ID',
    -- 变更记录
    `NUM_FROM`     INTEGER COMMENT '「numFrom」- 之前数量',
    `NUM_TO`       INTEGER COMMENT '「numTo」- 之后数量',
    `AMOUNT_FROM`  DECIMAL(18, 2) COMMENT '「amountFrom」- 之前平均价格',
    `AMOUNT_TO`    DECIMAL(18, 2) COMMENT '「amountTo」- 之后平均价格',
    `ITEM_ID`      VARCHAR(36) COMMENT '「itemId」- 入库/出库明细ID',

    -- 特殊字段
    `ACTIVE`       BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`        VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`     VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-commodity-his-2
ALTER TABLE P_COMMODITY_HIS
    ADD UNIQUE (`CODE`, `SIGMA`); -- 商品编号不重复