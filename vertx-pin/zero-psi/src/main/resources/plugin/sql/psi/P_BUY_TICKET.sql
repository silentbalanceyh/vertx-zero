-- liquibase formatted sql

-- changeset Lang:p-buy-ticket-1
-- 采购申请表：P_BUY_TICKET
DROP TABLE IF EXISTS P_BUY_TICKET;
CREATE TABLE IF NOT EXISTS P_BUY_TICKET
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 采购申请主键',
    `SERIAL`      VARCHAR(255) COMMENT '「serial」- 采购单号（系统可用）',
    `TYPE`        VARCHAR(36) COMMENT '「type」- 单据类型',
    `STATUS`      VARCHAR(36) COMMENT '「status」- 单据状态',
    -- 申请基本信息
    `OP_AT`       DATETIME COMMENT '「opAt」- 申请时间（单据日期）',
    `OP_BY`       VARCHAR(36) COMMENT '「opBy」- 申请人',
    `OP_DEPT`     VARCHAR(255) COMMENT '「opDept」- 申请部门',

    -- 单据标签
    `SOURCE`      VARCHAR(36) COMMENT '「source」- 单据来源',
    `CUSTOMER_ID` VARCHAR(36) COMMENT '「customerId」- 建议供应商',
    `CURRENCY_ID` VARCHAR(36) COMMENT '「currencyId」- 币种',
    `COMPANY_ID`  VARCHAR(36) COMMENT '「companyId」- 所属公司',

    -- 审核信息
    `APPROVED_BY` VARCHAR(36) COMMENT '「approvedBy」- 审核人',
    `APPROVED_AT` DATETIME COMMENT '「approvedAt」- 审核时间',
    `TO_ID`       VARCHAR(36) COMMENT '「toId」- 收货地址ID',
    `TO_ADDRESS`  TEXT COMMENT '「toAddress」- 收货地址',
    `TAGS`        TEXT COMMENT '「tags」- 单据标签',
    `COMMENT`     TEXT COMMENT '「comment」- 单据备注',

    -- 特殊字段
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人', -- 制单人
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人', -- 最后修改人
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-buy-ticket-2
ALTER TABLE P_BUY_TICKET
    ADD UNIQUE (`SERIAL`, `SIGMA`); -- 申请单号不重复