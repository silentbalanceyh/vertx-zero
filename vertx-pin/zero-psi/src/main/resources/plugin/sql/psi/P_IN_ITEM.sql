-- liquibase formatted sql

-- changeset Lang:p-in-item-1
-- 入库明细表：P_IN_ITEM
DROP TABLE IF EXISTS P_IN_ITEM;
CREATE TABLE IF NOT EXISTS P_IN_ITEM
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 入库明细主键',
    `SERIAL`          VARCHAR(255) COMMENT '「serial」- 入库明细号（系统可用，直接计算）',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 明细状态',

    -- 入库商品明细
    `COMMODITY_ID`    VARCHAR(36) COMMENT '「commodityId」- 商品ID',
    `COMMODITY_CODE`  VARCHAR(255) COMMENT '「commodityCode」- 商品编码',
    `COMMODITY_NAME`  VARCHAR(255) COMMENT '「commodityName」- 商品名称',
    `COMMODITY_LOGO`  VARCHAR(255) COMMENT '「commodityLogo」- 商品Logo',
    `CUSTOMER_ID`     VARCHAR(36) COMMENT '「customerId」- 供应商',
    `FROM_NAME`       VARCHAR(255) COMMENT '「fromName」- 供应商商品名称',
    `FROM_CODE`       VARCHAR(255) COMMENT '「fromCode」- 供应商商品编码',

    -- 采购详细信息
    `TICKET_ID`       VARCHAR(36) COMMENT '「ticketId」- 入库单ID',
    `ORDER_NUMBER`    VARCHAR(36) COMMENT '「orderNumber」- 订单编号',
    `ORDER_SOURCE`    VARCHAR(36) COMMENT '「orderSource」- 源单单号',
    `WH_ID`           VARCHAR(36) COMMENT '「whId」- 实际入库仓库',
    -- 数量
    `NUM_WAIT`        INT COMMENT '「numWait」- 预计入库',
    `NUM`             INT COMMENT '「num」- 实际入库数量',
    -- 入库单明细相关
    `TAX_RATE`        DECIMAL(10, 2) COMMENT '「taxRate」- 税率',
    `TAX_AMOUNT`      DECIMAL(18, 2) COMMENT '「taxAmount」- 税额',
    `TAX_PRICE`       DECIMAL(18, 2) COMMENT '「taxPrice」- 含税单价',
    `AMOUNT_TOTAL`    DECIMAL(18, 2) COMMENT '「amountTotal」- 税价合计',
    `AMOUNT_SPLIT`    DECIMAL(18, 2) COMMENT '「amountSplit」- 采购分摊费用',
    `AMOUNT`          DECIMAL(18, 2) COMMENT '「amount」- 采购总价（订单总价）',
    `PRICE`           DECIMAL(18, 2) COMMENT '「price」- 采购单价（采购价）',
    `COMMENT`         TEXT COMMENT '「comment」- 商品行备注',
    -- 成本计算
    `COST_PER_BASIS`  DECIMAL(18, 2) COMMENT '「costPerBasis」- 基本单位成本',
    `COST_PER`        DECIMAL(18, 2) COMMENT '「costPer」- 单位成本',
    `COST_AMOUNT`     DECIMAL(18, 2) COMMENT '「costAmount」- 入库成本',
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
-- changeset Lang:p-in-item-2
ALTER TABLE P_IN_ITEM
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 单号不重复