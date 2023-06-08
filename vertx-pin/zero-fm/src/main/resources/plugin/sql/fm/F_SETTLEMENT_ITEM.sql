-- liquibase formatted sql

-- changeset Lang:f-settlement-item-1
DROP TABLE IF EXISTS `F_SETTLEMENT_ITEM`;
CREATE TABLE `F_SETTLEMENT_ITEM`
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 结算单明细主键',
    `NAME`          VARCHAR(255)   NOT NULL COMMENT '「name」 -  明细名称',
    `CODE`          VARCHAR(255)   NOT NULL COMMENT '「code」 - 明细系统代码',
    `SERIAL`        VARCHAR(255)   NOT NULL COMMENT '「serial」 - 明细编号',
    `INCOME`        BIT COMMENT '「income」- true = 消费类，false = 付款类',

    -- 明细数据
    `TYPE`          VARCHAR(36)    NOT NULL COMMENT '「type」- 明细类型',
    `AMOUNT`        DECIMAL(18, 2) NOT NULL COMMENT '「amount」——价税合计，实际结算金额',
    `COMMENT`       LONGTEXT COMMENT '「comment」 - 明细备注',
    `MANUAL_NO`     VARCHAR(255) COMMENT '「manualNo」 - 手工单号（线下单号专用）',
    `PAY_TERM_ID`   VARCHAR(36)    NOT NULL COMMENT '「payTermId」- 账单项ID',

    -- 结算基础信息
    `RELATED_ID`    VARCHAR(36) COMMENT '「relatedId」- 关联BillItem ID（保留，原系统存在）',
    `SETTLEMENT_ID` VARCHAR(36) COMMENT '「settlementId」- 结算单ID，该字段有值标识已经结算',
    `DEBT_ID`       VARCHAR(36) COMMENT '「debtId」- 应收账单ID',
    `INVOICE_ID`    VARCHAR(36) COMMENT '「invoiceId」- 开票ID',

    -- 特殊字段
    `SIGMA`         VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:f-settlement-item-2
ALTER TABLE F_SETTLEMENT_ITEM
    ADD UNIQUE (`CODE`, `SETTLEMENT_ID`, `SIGMA`);
ALTER TABLE F_SETTLEMENT_ITEM
    ADD UNIQUE (`SERIAL`, `SETTLEMENT_ID`, `SIGMA`);

-- INDEX
ALTER TABLE F_SETTLEMENT_ITEM
    ADD INDEX IDX_F_SETTLEMENT_ITEM_DEBT_ID (`DEBT_ID`);
ALTER TABLE F_SETTLEMENT_ITEM
    ADD INDEX IDX_F_SETTLEMENT_ITEM_SETTLEMENT_ID (`SETTLEMENT_ID`);