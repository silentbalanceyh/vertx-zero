-- liquibase formatted sql

-- changeset Lang:p-out-ticket-1
-- 出库单：P_OUT_TICKET
DROP TABLE IF EXISTS P_OUT_TICKET;
CREATE TABLE IF NOT EXISTS P_OUT_TICKET
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 出库单主键',
    `SERIAL`        VARCHAR(255) COMMENT '「serial」- 出库单号（系统可用，直接计算）',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 单据类型',
    `TYPE_BUSINESS` VARCHAR(36) COMMENT '「typeBusiness」- 业务类型',
    `STATUS`        VARCHAR(36) COMMENT '「status」- 订单状态',

    -- 采购供应商
    `WH_ID`         VARCHAR(36) COMMENT '「whId」- 出库仓库',
    `CUSTOMER_ID`   VARCHAR(36) COMMENT '「customerId」- 客户ID',
    `CUSTOMER_INFO` TEXT COMMENT '「customerInfo」- 客户联系信息',
    -- 处理人员信息
    `OP_AT`         DATETIME COMMENT '「opAt」- 单据日期',
    `OP_BY`         VARCHAR(36) COMMENT '「opBy」- 业务员',
    `OP_DEPT`       VARCHAR(255) COMMENT '「opDept」- 业务部门',

    `TAGS`          TEXT COMMENT '「tags」- 单据标签',
    `COMMENT`       TEXT COMMENT '「comment」- 单据备注',

    -- 金额信息
    `AMOUNT`        DECIMAL(18, 2) COMMENT '「amount」- 出库单总额',

    -- 关联信息
    `APPROVED_BY`   VARCHAR(36) COMMENT '「approvedBy」- 审核人',
    `APPROVED_AT`   DATETIME COMMENT '「approvedAt」- 审核时间',
    `TO_ID`         VARCHAR(36) COMMENT '「toId」- 收货地址ID',
    `TO_ADDRESS`    TEXT COMMENT '「toAddress」- 收货地址',
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
-- changeset Lang:p-out-ticket-2
ALTER TABLE P_OUT_TICKET
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 订单号不重复