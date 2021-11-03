-- liquibase formatted sql

-- changeset Lang:p-buy-order-1
-- 采购订单表：P_BUY_ORDER
DROP TABLE IF EXISTS P_BUY_ORDER;
CREATE TABLE IF NOT EXISTS P_BUY_ORDER
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 采购订单主键',
    `SERIAL`          VARCHAR(255) COMMENT '「serial」- 采购订单号（系统可用，直接计算）',
    `TYPE`            VARCHAR(36) COMMENT '「type」- 单据类型',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 订单状态',

    -- 采购供应商
    `WH_ID`           VARCHAR(36) COMMENT '「whId」- 建议入库仓库',
    `CUSTOMER_ID`     VARCHAR(36) COMMENT '「customerId」- 采购供应商',
    `PAYED_AT`        DATETIME COMMENT '「payedAt」- 结算日期',
    `PAYED_DAY`       INT COMMENT '「payedDay」- 采购期限',
    -- 处理人员信息
    `OP_AT`           DATETIME COMMENT '「opAt」- 单据日期',
    `OP_BY`           VARCHAR(36) COMMENT '「opBy」- 业务员',
    `OP_DEPT`         VARCHAR(255) COMMENT '「opDept」- 业务部门',

    `TAGS`            TEXT COMMENT '「tags」- 单据标签',
    `COMMENT`         TEXT COMMENT '「comment」- 单据备注',
    `STATUS_SEND`     VARCHAR(36) COMMENT '「statusSend」- 发送状态',
    `REASON`          TEXT COMMENT '「reason」- 发送失败原因',

    -- 金额信息
    `AMOUNT`          DECIMAL(18, 2) COMMENT '「amount」- 订单总额',
    `AMOUNT_WAIT`     DECIMAL(18, 2) COMMENT '「amountWait」- 应付余额',
    `AMOUNT_DEBT`     DECIMAL(18, 2) COMMENT '「amountDebt」- 上次欠款',
    `AMOUNT_PLAN`     DECIMAL(18, 2) COMMENT '「amountPlan」- 预计采购费用',
    `AMOUNT_YES`      DECIMAL(18, 2) COMMENT '「amountYes」- 成交金额',

    -- 折扣
    `DISCOUNT_AMOUNT` DECIMAL(18, 2) COMMENT '「discountAmount」- 整单折扣额',
    `DISCOUNT_RATE`   DECIMAL(10, 2) COMMENT '「discountRate」- 折扣率',
    `DISCOUNT`        BIT         DEFAULT NULL COMMENT '「discount」- 定向折扣',

    -- 关联信息
    `APPROVED_BY`     VARCHAR(36) COMMENT '「approvedBy」- 审核人',
    `APPROVED_AT`     DATETIME COMMENT '「approvedAt」- 审核时间',
    `TO_ID`           VARCHAR(36) COMMENT '「toId」- 收货地址ID',
    `TO_ADDRESS`      TEXT COMMENT '「toAddress」- 收货地址',
    `FROM_ID`         VARCHAR(36) COMMENT '「fromId」- 发货地址ID',
    `FROM_ADDRESS`    TEXT COMMENT '「fromAddress」- 发货地址',
    `CURRENCY_ID`     VARCHAR(36) COMMENT '「currencyId」- 币种',
    `COMPANY_ID`      VARCHAR(36) COMMENT '「companyId」- 所属公司',

    -- 特殊字段
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人', -- 制单人
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人', -- 最后修改人
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-buy-order-2
ALTER TABLE P_BUY_ORDER
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 订单号不重复