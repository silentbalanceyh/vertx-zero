-- liquibase formatted sql

-- changeset Lang:p-in-ticket-1
-- 入库单：P_IN_TICKET
DROP TABLE IF EXISTS P_IN_TICKET;
CREATE TABLE IF NOT EXISTS P_IN_TICKET
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 入库单主键',
    `SERIAL`        VARCHAR(255) COMMENT '「serial」- 入库单号（系统可用，直接计算）',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 单据类型',
    `TYPE_BUSINESS` VARCHAR(36) COMMENT '「typeBusiness」- 业务类型',
    `STATUS`        VARCHAR(36) COMMENT '「status」- 订单状态',

    -- 采购供应商
    `WH_ID`         VARCHAR(36) COMMENT '「whId」- 建议入库仓库',
    `CUSTOMER_ID`   VARCHAR(36) COMMENT '「customerId」- 实际供应商',
    `CUSTOMER_INFO` TEXT COMMENT '「customerInfo」- 实际供应商联系信息',
    `PAYED_AT`      DATETIME COMMENT '「payedAt」- 结算日期',
    `PAYED_DAY`     INT COMMENT '「payedDay」- 采购期限',
    -- 处理人员信息
    `OP_AT`         DATETIME COMMENT '「opAt」- 单据日期',
    `OP_BY`         VARCHAR(36) COMMENT '「opBy」- 业务员',
    `OP_DEPT`       VARCHAR(255) COMMENT '「opDept」- 业务部门',

    `TAGS`          TEXT COMMENT '「tags」- 单据标签',
    `COMMENT`       TEXT COMMENT '「comment」- 单据备注',
    `SOURCE`        VARCHAR(36) COMMENT '「source」- 单据来源',

    -- 金额信息
    `TAX_AMOUNT`    DECIMAL(18, 2) COMMENT '「taxAmount」- 税额',
    `AMOUNT`        DECIMAL(18, 2) COMMENT '「amount」- 入库单总额',
    `AMOUNT_TOTAL`  DECIMAL(18, 2) COMMENT '「amountTotal」- 税价合计',

    -- 关联信息
    `APPROVED_BY`   VARCHAR(36) COMMENT '「approvedBy」- 审核人',
    `APPROVED_AT`   DATETIME COMMENT '「approvedAt」- 审核时间',
    `TO_ID`         VARCHAR(36) COMMENT '「toId」- 收货地址ID',
    `TO_ADDRESS`    TEXT COMMENT '「toAddress」- 收货地址',
    `FROM_ID`       VARCHAR(36) COMMENT '「fromId」- 发货地址ID',
    `FROM_ADDRESS`  TEXT COMMENT '「fromAddress」- 发货地址',
    `CURRENCY_ID`   VARCHAR(36) COMMENT '「currencyId」- 币种',
    `COMPANY_ID`    VARCHAR(36) COMMENT '「companyId」- 所属公司',

    -- 特殊字段
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人', -- 制单人
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人', -- 最后修改人
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-in-ticket-2
ALTER TABLE P_IN_TICKET
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 订单号不重复