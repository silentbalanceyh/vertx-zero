-- liquibase formatted sql


-- changeset Lang:f-settlement-1
DROP TABLE IF EXISTS `F_SETTLEMENT`;
CREATE TABLE `F_SETTLEMENT`
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 结算单主键',
    `CODE`        VARCHAR(255)   NOT NULL COMMENT '「code」 - 结算单编号',
    `SERIAL`      VARCHAR(36)    NOT NULL COMMENT '「serial」 - 结算单据号',

    -- 基本信息
    `AMOUNT`      DECIMAL(18, 2) NOT NULL COMMENT '「amount」——价税合计，所有明细对应的实际结算金额',
    `COMMENT`     LONGTEXT COMMENT '「comment」 - 结算单备注',
    `ROUNDED`     VARCHAR(12) COMMENT '「rounded」抹零方式：四舍五入, HALF：零头舍掉, FLOOR, 零头入进, CEIL',
    `FINISHED`    BIT COMMENT '「finished」- 是否完成',
    `FINISHED_AT` DATETIME COMMENT '「createdAt」- 完成时间',
    `SIGN_NAME`   VARCHAR(128) DEFAULT NULL COMMENT '「signName」签单人姓名',
    `SIGN_MOBILE` VARCHAR(128) DEFAULT NULL COMMENT '「signMobile」签单人电话',

    -- 关联信息
    `CUSTOMER_ID` VARCHAR(36)  DEFAULT NULL COMMENT '「customerId」结算对象（单位ID）',
    `ORDER_ID`    VARCHAR(36)  DEFAULT NULL COMMENT '「orderId」- 预授权所属订单ID',
    `BOOK_ID`     VARCHAR(36)  DEFAULT NULL COMMENT '「bookId」- 所属账本ID',

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
-- changeset Lang:f-settlement-2
ALTER TABLE F_SETTLEMENT
    ADD UNIQUE (`CODE`, `ORDER_ID`, `SIGMA`);
ALTER TABLE F_SETTLEMENT
    ADD UNIQUE (`SERIAL`, `ORDER_ID`, `SIGMA`);
ALTER TABLE F_SETTLEMENT
    ADD INDEX IDX_F_SETTLEMENT_ORDER_ID (`ORDER_ID`);
ALTER TABLE F_SETTLEMENT
    ADD INDEX IDX_F_SETTLEMENT_BOOK_ID (`BOOK_ID`);

