-- liquibase formatted sql


-- changeset Lang:f-receivable-1
DROP TABLE IF EXISTS `F_RECEIVABLE`;
CREATE TABLE `F_RECEIVABLE`
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 应收账单主键ID',
    `NAME`        VARCHAR(255)   NOT NULL COMMENT '「name」 - 应收单标题',
    `CODE`        VARCHAR(255)   NOT NULL COMMENT '「code」 - 应收单编号',
    `SERIAL`      VARCHAR(36)    NOT NULL COMMENT '「serial」 - 应收单据号',

    -- 基本信息
    `AMOUNT`      DECIMAL(18, 2) NOT NULL COMMENT '「amount」——价税合计，所有明细对应的实际结算金额',
    `COMMENT`     LONGTEXT COMMENT '「comment」 - 结算单备注',
    `SIGN_NAME`   VARCHAR(128) DEFAULT NULL COMMENT '「signName」签单人姓名',
    `SIGN_MOBILE` VARCHAR(128) DEFAULT NULL COMMENT '「signMobile」签单人电话',
    `FINISHED`    BIT COMMENT '「finished」- 是否完成',
    `FINISHED_AT` DATETIME COMMENT '「createdAt」- 完成时间',

    -- 关联信息
    `CUSTOMER_ID` VARCHAR(36)  DEFAULT NULL COMMENT '「customerId」结算对象（单位ID）',

    -- 特殊字段
    `SIGMA`       VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`    VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`      BIT COMMENT '「active」- 是否启用',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:f-receivable-2
ALTER TABLE F_RECEIVABLE
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE F_RECEIVABLE
    ADD UNIQUE (`SERIAL`, `SIGMA`);
