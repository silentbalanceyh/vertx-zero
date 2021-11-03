-- liquibase formatted sql

-- changeset Lang:p-buy-item-1
-- 采购明细表：P_BUY_ITEM
DROP TABLE IF EXISTS P_BUY_ITEM;
CREATE TABLE IF NOT EXISTS P_BUY_ITEM
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 采购明细主键',
    `SERIAL`          VARCHAR(255) COMMENT '「serial」- 采购单号（系统可用，直接计算）',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 明细状态',

    -- 采购商品信息
    `COMMODITY_ID`    VARCHAR(36) COMMENT '「commodityId」- 商品ID',
    `COMMODITY_CODE`  VARCHAR(255) COMMENT '「commodityCode」- 商品编码',
    `COMMODITY_NAME`  VARCHAR(255) COMMENT '「commodityName」- 商品名称',
    `CUSTOMER_ID`     VARCHAR(36) COMMENT '「customerId」- 建议供应商',

    -- 采购详细信息
    `TICKET_ID`       VARCHAR(36) COMMENT '「ticketId」- 采购申请ID',
    `ORDER_ID`        VARCHAR(36) COMMENT '「orderId」- 采购订单ID',

    -- 采购数量
    `NUM_REQUEST`     INT COMMENT '「numRequest」- 申请数量',
    `NUM_APPROVED`    INT COMMENT '「numApproved」- 审批数量',
    `NUM`             INT COMMENT '「num」- 实际采购数量（订单中）',
    -- 价格信息
    `TAX_RATE`        DECIMAL(10, 2) COMMENT '「taxRate」- 税率',
    `TAX_AMOUNT`      DECIMAL(18, 2) COMMENT '「taxAmount」- 税额',
    `TAX_PRICE`       DECIMAL(18, 2) COMMENT '「taxPrice」- 含税单价',
    `AMOUNT_TOTAL`    DECIMAL(18, 2) COMMENT '「amountTotal」- 税价合计',
    `AMOUNT_SPLIT`    DECIMAL(18, 2) COMMENT '「amountSplit」- 采购分摊费用',
    `AMOUNT`          DECIMAL(18, 2) COMMENT '「amount」- 采购总价（订单总价）',
    `PRICE`           DECIMAL(18, 2) COMMENT '「price」- 采购单价（采购价）',

    -- 折扣
    `DISCOUNT_AMOUNT` DECIMAL(18, 2) COMMENT '「discountAmount」- 折扣金额',
    `DISCOUNT_RATE`   DECIMAL(10, 2) COMMENT '「discountRate」- 折扣率',

    -- 数量信息
    `COMMENT`         TEXT COMMENT '「comment」- 商品行备注',
    `ARRIVE_AT`       DATETIME COMMENT '「arriveAt」- 预计到货时间',

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
-- changeset Lang:p-buy-item-2
ALTER TABLE P_BUY_ITEM
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 申请单号不重复