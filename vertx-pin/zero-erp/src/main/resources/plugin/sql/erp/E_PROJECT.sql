-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-project-1
-- ----------------------------
-- Table structure for E_PROJECT
-- ----------------------------
DROP TABLE IF EXISTS `E_PROJECT`;
CREATE TABLE `E_PROJECT`
(
    `KEY`           VARCHAR(36) NOT NULL COMMENT '「key」- 项目ID',
    `NAME`          VARCHAR(255) COMMENT '「name」- 项目名称',
    `CODE`          VARCHAR(255) COMMENT '「code」- 项目编号',
    `SHORT_NAME`    VARCHAR(255) COMMENT '「shortName」- 项目简称',

    -- 关联字典
    `DEPT_ID`       VARCHAR(36) COMMENT '「deptId」- 所属部门, resource.departments',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 项目分类，zero.project',
    `STATUS`        VARCHAR(36) COMMENT '「status」- 项目状态，zero.project.status',
    `BUDGET`        VARCHAR(36) COMMENT '「budget」- 所属预算，zero.project.budget',
    `LEVEL`         VARCHAR(36) COMMENT '「level」- 项目级别，zero.project.level',
    `PRIORITY`      VARCHAR(36) COMMENT '「priority」- 项目优先级，zero.project.priority',
    `RISK`          VARCHAR(36) COMMENT '「risk」- 项目风险，zero.project.risk',


    -- 项目基本信息
    `TITLE`         VARCHAR(1024) COMMENT '「title」- 项目标题',
    `ICON`          VARCHAR(255) COMMENT '「icon」- 项目图标',
    `AMOUNT`        DECIMAL(18, 2) COMMENT '「amount」- 项目金额',
    `SUBJECT`       TEXT COMMENT '「subject」- 项目目标',
    `DESCRIPTION`   LONGTEXT COMMENT '「description」- 主单描述内容',
    `REMARK`        LONGTEXT COMMENT '「remark」- 项目备注',
    `LEAD_BY`       VARCHAR(36) COMMENT '「leadBy」- 项目经理',

    -- 时间信息
    `PLAN_START_AT` DATETIME COMMENT '「planStartAt」- 开始日期',
    `PLAN_END_AT`   DATETIME COMMENT '「planEndAt」- 结束日期',
    `START_AT`      DATETIME COMMENT '「startAt」- 实际开始日期',
    `END_AT`        DATETIME COMMENT '「endAt」- 实际结束日期',

    -- 特殊字段
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识（项目所属应用）',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:h-project-2
ALTER TABLE E_PROJECT
    ADD UNIQUE (`CODE`, `SIGMA`);