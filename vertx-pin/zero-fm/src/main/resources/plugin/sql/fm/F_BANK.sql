-- liquibase formatted sql

-- changeset Lang:f-bank-1
DROP TABLE IF EXISTS `F_BANK`;
CREATE TABLE `F_BANK`
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 银行主键',
    `NAME`        VARCHAR(255) COMMENT '「name」- 银行名称',
    `CODE`        VARCHAR(255) NOT NULL COMMENT '「code」- 银行系统编号',

    -- 银行基本信息
    `ALIAS`       VARCHAR(255) COMMENT '「alias」- 银行别称',
    `LOGO`        VARCHAR(255) COMMENT '「logo」- 银行图标',
    `WEBSITE`     VARCHAR(255) COMMENT '「website」- 银行主页',
    `COMMENT`     LONGTEXT COMMENT '「comment」 - 银行备注',

    -- 关联信息
    `BRANCH_NAME` VARCHAR(255) COMMENT '「branchName」- 支行名称',
    `BRANCH_CODE` VARCHAR(255) COMMENT '「branchCode」- 支行编号',
    `BRANCH`      BIT COMMENT '「branch」- 是否支行',
    `BANK_ID`     VARCHAR(36) COMMENT '「bankId」- 所属银行ID',
    `LOCATION_ID` VARCHAR(36) COMMENT '「locationId」- 银行地址对应信息',

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
-- changeset Lang:f-bank-2
ALTER TABLE F_BANK
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE F_BANK
    ADD UNIQUE (`BRANCH_CODE`, `SIGMA`);
ALTER TABLE F_BANK
    ADD UNIQUE (`BRANCH_NAME`, `SIGMA`);