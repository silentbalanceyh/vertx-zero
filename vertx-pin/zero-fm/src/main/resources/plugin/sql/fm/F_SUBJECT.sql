-- liquibase formatted sql

-- changeset Lang:f-subject-1
DROP TABLE IF EXISTS `F_SUBJECT`;
CREATE TABLE `F_SUBJECT`
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 会计科目ID',
    `NAME`       VARCHAR(255) NOT NULL COMMENT '「name」 -  会计科目名称',
    `CODE`       VARCHAR(255) DEFAULT NULL COMMENT '「code」 - 会计科目系统编号',
    `SERIAL`     VARCHAR(128) DEFAULT NULL COMMENT '「serial」 - 会计科目编号',

    -- 维度信息
    `CATEGORY`   VARCHAR(36) COMMENT '「category」 - 会计科目关联类别，直接对接类型树',

    -- 辅助信息
    `HELP_CODE`  VARCHAR(32) COMMENT '「helpCode」- 会计科目助记码',
    `COMMENT`    LONGTEXT COMMENT '「comment」 - 会计科目备注',
    `OWNER`      VARCHAR(32) COMMENT '「owner」- 科目方向：OUT-借方 / IN-贷方',

    -- 关联信息
    `COMPANY_ID` VARCHAR(36) COMMENT '「companyId」- 会计科目所属公司',
    `PARENT_ID`  VARCHAR(36) COMMENT '「parentId」- 会计科目父科目',

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
-- changeset Lang:f-subject-2
ALTER TABLE F_SUBJECT
    ADD UNIQUE (`CODE`, `SIGMA`);