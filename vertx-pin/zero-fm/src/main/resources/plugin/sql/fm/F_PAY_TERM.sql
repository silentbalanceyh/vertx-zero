-- liquibase formatted sql

-- changeset Lang:f-pay-term-1
DROP TABLE IF EXISTS `F_PAY_TERM`;
CREATE TABLE `F_PAY_TERM`
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 账单项',
    `NAME`       VARCHAR(32)  NOT NULL COMMENT '「name」- 账单项名称',
    `CODE`       VARCHAR(255) NOT NULL COMMENT '「code」- 账单项编号',

    -- 维度信息
    `TYPE`       VARCHAR(36)  NOT NULL COMMENT '「type」- 账单项类型：付款类/消费类',
    `CATEGORY`   VARCHAR(36)  NOT NULL COMMENT '「category」- 账单项类别',

    -- 基本信息
    `HELP_CODE`  VARCHAR(32) COMMENT '「helpCode」- 助记码',
    `LEAF`       BIT COMMENT '「leaf」- 是否明细',
    `COMMENT`    LONGTEXT COMMENT '「comment」 - 账单项备注',

    -- 关联信息
    `PARENT_ID`  VARCHAR(36) COMMENT '「parentId」- 父账单项',
    `SUBJECT_ID` VARCHAR(36) COMMENT '「subjectId」- 会计科目关联ID',
    `LOCKED`     BIT COMMENT '「locked」- 是否锁定',

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
-- changeset Lang:f-pay-term-2
ALTER TABLE F_PAY_TERM
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE F_PAY_TERM
    ADD UNIQUE (`NAME`, `SIGMA`);