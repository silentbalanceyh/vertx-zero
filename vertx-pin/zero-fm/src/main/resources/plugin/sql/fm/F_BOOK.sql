-- liquibase formatted sql

-- changeset Lang:f-book-1
DROP TABLE IF EXISTS `F_BOOK`;
CREATE TABLE `F_BOOK`
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 账本主键ID',
    `NAME`         VARCHAR(255) COMMENT '「name」 - 账本名称',
    `CODE`         VARCHAR(255)   NOT NULL COMMENT '「code」 - 账本的系统编号',
    `SERIAL`       VARCHAR(36)    NOT NULL COMMENT '「serial」 - 财务系统账本编号',

    -- 维度信息
    `TYPE`         VARCHAR(36)    NOT NULL COMMENT '「type」 - 账本类型',
    `STATUS`       VARCHAR(36)    NOT NULL COMMENT '「status」 - 账本状态',
    `MAJOR`        BIT COMMENT '「major」- 主账本标识',

    -- 基本信息
    `AMOUNT`       DECIMAL(18, 2) NOT NULL COMMENT '「amount」- 交易金额，正数：应收，负数：应退，最终计算总金额',
    `COMMENT`      LONGTEXT COMMENT '「comment」 - 账本备注',
    -- 加收、检查
    `CHECKED`      BIT COMMENT '「checked」- 是否检查',
    `CHECKED_DESC` LONGTEXT COMMENT '「checkedDesc」 - 账本检查描述信息',
    `EXCEED`       BIT COMMENT '「exceed」- 是否加收',
    `EXCEED_DESC`  LONGTEXT COMMENT '「exceedDesc」 - 账本加收描述信息',

    -- 关联信息
    `MODEL_ID`     VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`    VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `PARENT_ID`    VARCHAR(36) COMMENT '「parentId」- 子账本专用，引用父账本ID',
    `ORDER_ID`     VARCHAR(36) COMMENT '「orderId」- 订单对应的订单ID',

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
-- changeset Lang:f-book-2
ALTER TABLE F_BOOK
    ADD UNIQUE (`CODE`, `ORDER_ID`, `SIGMA`);
ALTER TABLE F_BOOK
    ADD UNIQUE (`SERIAL`, `ORDER_ID`, `SIGMA`);
-- INDEX
ALTER TABLE F_BOOK
    ADD INDEX IDX_F_BOOK_ORDER_ID (`ORDER_ID`);
