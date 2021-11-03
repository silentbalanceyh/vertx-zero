-- liquibase formatted sql

-- changeset Lang:f-currency-1
DROP TABLE IF EXISTS `F_CURRENCY`;
CREATE TABLE `F_CURRENCY`
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 币种主键',
    `NAME`         VARCHAR(255) COMMENT '「name」- 币种名称',
    `CODE`         VARCHAR(255) NOT NULL COMMENT '「code」- 币种编码',

    -- 币种信息
    `COMMENT`      LONGTEXT COMMENT '「comment」 - 币种备注',
    `DIGIT_AMOUNT` INTEGER COMMENT '「digitAmount」- 金额小数位数',
    `DIGIT_PRICE`  INTEGER COMMENT '「digitPrice」- 单价小数位数',

    -- 特殊字段
    `SIGMA`        VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`     VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`       BIT COMMENT '「active」- 是否启用',
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:f-currency-2
ALTER TABLE F_CURRENCY
    ADD UNIQUE (`CODE`, `SIGMA`);