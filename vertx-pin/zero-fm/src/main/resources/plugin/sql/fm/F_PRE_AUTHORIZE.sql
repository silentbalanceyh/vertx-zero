-- liquibase formatted sql

-- changeset Lang:f-pre-authorize-1
DROP TABLE IF EXISTS `F_PRE_AUTHORIZE`;
CREATE TABLE `F_PRE_AUTHORIZE`
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 预授权ID',
    `CODE`       VARCHAR(255)   NOT NULL COMMENT '「code」 - 预授权系统编号',
    `SERIAL`     VARCHAR(36)    NOT NULL COMMENT '「serial」 - 预授权单据号',
    `STATUS`     VARCHAR(36)    NOT NULL COMMENT '「status」 - 预授权状态，Lock/Unlock',

    -- 基本信息
    `AMOUNT`     DECIMAL(18, 2) NOT NULL COMMENT '「amount」- 当前预授权刷单金额',
    `COMMENT`    LONGTEXT COMMENT '「comment」 - 预授权备注',
    `EXPIRED_AT` DATETIME    DEFAULT NULL COMMENT '「expiredAt」——预授权有效期',
    `BANK_NAME`  VARCHAR(255)   NOT NULL COMMENT '「bankName」- 预授权银行名称',
    `BANK_CARD`  VARCHAR(255)   NOT NULL COMMENT '「bankCard」- 刷预授权的银行卡号',

    -- 关联信息
    `ORDER_ID`   VARCHAR(36) DEFAULT NULL COMMENT '「orderId」- 预授权所属订单ID',
    `BILL_ID`    VARCHAR(36) DEFAULT NULL COMMENT '「billId」- 预授权所属账单ID',
    `BOOK_ID`    VARCHAR(36) DEFAULT NULL COMMENT '「bookId」- 所属账本ID',

    -- 特殊字段
    `SIGMA`      VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`   VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`     BIT COMMENT '「active」- 是否启用',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:f-pre-authorize-2
ALTER TABLE F_PRE_AUTHORIZE
    ADD UNIQUE (`CODE`, `BILL_ID`, `SIGMA`);
ALTER TABLE F_PRE_AUTHORIZE
    ADD UNIQUE (`SERIAL`, `BILL_ID`, `SIGMA`);

ALTER TABLE F_PRE_AUTHORIZE
    ADD INDEX IDX_F_PRE_AUTHORIZE_ORDER_ID (`ORDER_ID`);
ALTER TABLE F_PRE_AUTHORIZE
    ADD INDEX IDX_F_PRE_AUTHORIZE_BOOK_ID (`BOOK_ID`);