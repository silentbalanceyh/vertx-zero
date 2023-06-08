-- liquibase formatted sql

-- changeset Lang:f-bill-item-1
DROP TABLE IF EXISTS `F_BILL_ITEM`;
CREATE TABLE `F_BILL_ITEM`
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 账单明细主键',
    `NAME`          VARCHAR(255)   NOT NULL COMMENT '「name」 -  明细名称',
    `CODE`          VARCHAR(255)   NOT NULL COMMENT '「code」 - 明细系统代码',
    `SERIAL`        VARCHAR(255)   NOT NULL COMMENT '「serial」 - 明细编号',
    `INCOME`        BIT COMMENT '「income」- true = 消费类，false = 付款类',

    -- 维度信息
    `TYPE`          VARCHAR(36)    NOT NULL COMMENT '「type」- 明细类型',
    `STATUS`        VARCHAR(36)    NOT NULL COMMENT '「status」- 明细状态',

    -- 基本信息
    `AMOUNT`        DECIMAL(18, 2) DEFAULT NULL COMMENT '「amount」——价税合计，实际付款结果，有可能父项',
    `COMMENT`       LONGTEXT COMMENT '「comment」 - 明细备注',
    `MANUAL_NO`     VARCHAR(255) COMMENT '「manualNo」 - 手工单号（线下单号专用）',
    -- 商品信息
    `UNIT`          VARCHAR(36)    DEFAULT NULL COMMENT '「unit」- 计量单位',
    `PRICE`         DECIMAL(18, 2) NOT NULL COMMENT '「price」- 商品单价',
    `QUANTITY`      INT            NOT NULL COMMENT '「quantity」- 商品数量',
    `AMOUNT_TOTAL`  DECIMAL(18, 2) NOT NULL COMMENT '「amountTotal」——总价，理论计算结果',

    -- 人员部分
    `OP_BY`         VARCHAR(36) COMMENT '「opBy」- 操作人员，关联员工ID',
    `OP_NUMBER`     VARCHAR(36) COMMENT '「opNumber」- 操作人员工号',
    `OP_SHIFT`      VARCHAR(128) COMMENT '「opShift」- 操作班次（对接排班系统）',
    `OP_AT`         DATETIME COMMENT '「opAt」- 操作时间',

    -- 关联信息
    `RELATED_ID`    VARCHAR(36) COMMENT '「relatedId」- 关联ID（保留，原系统存在）',
    `SETTLEMENT_ID` VARCHAR(36) COMMENT '「settlementId」- 结算单ID，该字段有值标识已经结算',
    `BILL_ID`       VARCHAR(36)    NOT NULL COMMENT '「billId」- 所属账单ID',
    `SUBJECT_ID`    VARCHAR(36) COMMENT '「subjectId」- 会计科目ID，依赖账单项选择结果',
    `PAY_TERM_ID`   VARCHAR(36)    NOT NULL COMMENT '「payTermId」- 账单项ID',

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
-- changeset Lang:f-bill-item-2
ALTER TABLE F_BILL_ITEM
    ADD UNIQUE (`CODE`, `BILL_ID`, `SIGMA`);
ALTER TABLE F_BILL_ITEM
    ADD UNIQUE (`SERIAL`, `BILL_ID`, `SIGMA`);
-- INDEX
ALTER TABLE F_BILL_ITEM
    ADD INDEX IDX_F_BILL_ITEM_BILL_ID (`BILL_ID`);
ALTER TABLE F_BILL_ITEM
    ADD INDEX IDX_F_BILL_ITEM_SETTLEMENT_ID (`SETTLEMENT_ID`);