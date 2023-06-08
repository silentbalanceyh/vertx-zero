-- liquibase formatted sql

-- changeset Lang:f-bill-1
DROP TABLE IF EXISTS `F_BILL`;
CREATE TABLE `F_BILL`
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 账单主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 账单标题',
    `CODE`       VARCHAR(255)   NOT NULL COMMENT '「code」- 账单系统编号',
    `SERIAL`     VARCHAR(255)   NOT NULL COMMENT '「serial」- 账单流水线号',

    -- 维度信息
    `TYPE`       VARCHAR(36)    NOT NULL COMMENT '「type」- 账单类型',
    `CATEGORY`   VARCHAR(36)    NOT NULL COMMENT '「category」- 账单类别',

    -- 基本信息
    `AMOUNT`     DECIMAL(18, 2) NOT NULL COMMENT '「amount」- 账单金额',
    `INCOME`     BIT COMMENT '「income」- true = 消费类，false = 付款类',
    `COMMENT`    LONGTEXT COMMENT '「comment」 - 账单备注',

    -- 关联信息
    `ORDER_ID`   VARCHAR(36) COMMENT '「orderId」- 订单对应的订单ID',
    `BOOK_ID`    VARCHAR(36) COMMENT '「bookId」- 关联账本ID',
    `MODEL_ID`   VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`  VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',

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
-- changeset Lang:f-bill-2
ALTER TABLE F_BILL
    ADD UNIQUE (`CODE`, `ORDER_ID`, `SIGMA`);
ALTER TABLE F_BILL
    ADD UNIQUE (`SERIAL`, `ORDER_ID`, `SIGMA`);

-- INDEX
ALTER TABLE F_BILL
    ADD INDEX IDX_F_BILL_ORDER_ID (`ORDER_ID`);
ALTER TABLE F_BILL
    ADD INDEX IDX_F_BILL_BOOK_ID (`BOOK_ID`);
