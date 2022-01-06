-- liquibase formatted sql

-- changeset Lang:f-payment-1
DROP TABLE IF EXISTS `F_PAYMENT`;
CREATE TABLE `F_PAYMENT`
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 付款单账单主键ID',
    `NAME`       VARCHAR(255) COMMENT '「name」 - 付款单单标题',
    `CODE`       VARCHAR(255)   NOT NULL COMMENT '「code」 - 付款单单编号',
    `SERIAL`     VARCHAR(36)    NOT NULL COMMENT '「serial」 - 付款单单据号',

    -- 基本信息
    `AMOUNT`     DECIMAL(18, 2) NOT NULL COMMENT '「amount」——价税合计，所有明细对应的实际结算金额',
    `PAY_NAME`   VARCHAR(128) DEFAULT NULL COMMENT '「payName」打款人姓名',
    `PAY_MOBILE` VARCHAR(128) DEFAULT NULL COMMENT '「payMobile」打款人电话',
    `PAY_METHOD` VARCHAR(255) DEFAULT NULL COMMENT '「payMethod」付款方式',
    `PAY_ID`     VARCHAR(255) DEFAULT NULL COMMENT '「payId」付款账号',
    `COMMENT`    LONGTEXT COMMENT '「comment」 - 备注',
    -- 预付信息
    `PREPAY`     BIT COMMENT '「prepay」- 是否预付',

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
-- changeset Lang:f-payment-2
ALTER TABLE F_PAYMENT
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE F_PAYMENT
    ADD UNIQUE (`SERIAL`, `SIGMA`);
