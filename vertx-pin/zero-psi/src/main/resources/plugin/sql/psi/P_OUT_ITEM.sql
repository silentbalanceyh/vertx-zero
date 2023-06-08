-- liquibase formatted sql

-- changeset Lang:p-out-item-1
-- 出库明细表：P_OUT_ITEM
DROP TABLE IF EXISTS P_OUT_ITEM;
CREATE TABLE IF NOT EXISTS P_OUT_ITEM
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 出库明细主键',
    `SERIAL`          VARCHAR(255) COMMENT '「serial」- 出库明细号（系统可用，直接计算）',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 明细状态',

    -- 出库商品明细
    `COMMODITY_ID`    VARCHAR(36) COMMENT '「commodityId」- 商品ID',
    `COMMODITY_CODE`  VARCHAR(255) COMMENT '「commodityCode」- 商品编码',
    `COMMODITY_NAME`  VARCHAR(255) COMMENT '「commodityName」- 商品名称',
    `COMMODITY_LOGO`  VARCHAR(255) COMMENT '「commodityLogo」- 商品Logo',
    `CUSTOMER_ID`     VARCHAR(36) COMMENT '「customerId」- 供应商',
    `FROM_NAME`       VARCHAR(255) COMMENT '「fromName」- 供应商商品名称',
    `FROM_CODE`       VARCHAR(255) COMMENT '「fromCode」- 供应商商品编码',

    -- 销售详细信息
    `TICKET_ID`       VARCHAR(36) COMMENT '「ticketId」- 出库单ID',
    `WH_ID`           VARCHAR(36) COMMENT '「whId」- 实际出库仓库',
    -- 数量
    `NUM`             INT COMMENT '「num」- 实际出库数量',
    -- 出库单明细相关
    `AMOUNT`          DECIMAL(18, 2) COMMENT '「amount」- 销售总价（订单总价）',
    `PRICE`           DECIMAL(18, 2) COMMENT '「price」- 销售单价（销售价）',
    `COMMENT`         TEXT COMMENT '「comment」- 商品行备注',
    -- 成本计算
    `COST_PER_BASIS`  DECIMAL(18, 2) COMMENT '「costPerBasis」- 基本单位成本',
    `COST_PER`        DECIMAL(18, 2) COMMENT '「costPer」- 单位成本',
    `COST_AMOUNT`     DECIMAL(18, 2) COMMENT '「costAmount」- 出库成本',
    -- 折扣
    `DISCOUNT_AMOUNT` DECIMAL(18, 2) COMMENT '「discountAmount」- 折扣金额',
    `DISCOUNT_RATE`   DECIMAL(10, 2) COMMENT '「discountRate」- 折扣率',

    -- 特殊字段
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-out-item-2
ALTER TABLE P_OUT_ITEM
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 单号不重复