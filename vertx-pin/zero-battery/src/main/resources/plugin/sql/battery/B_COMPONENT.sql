-- liquibase formatted sql

-- changeset Lang:b-component-1
/*
 * BLOCK 中的界面资源定义（按页面分）
 * Java中的组件管理（Zero组件大盘点）
 * （管理端）
 */
DROP TABLE IF EXISTS B_COMPONENT;
CREATE TABLE IF NOT EXISTS B_COMPONENT
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 主键',
    `BLOCK_ID`       VARCHAR(36) COMMENT '「blockId」- 所属模块ID',
    /*
     * 类型综述
     * 1. SL，ServiceLoader组件
     * 2. INNER，Zero内部专用组件
     * 3. EXTENSION，Zero Extension专用组件
     * 4. JET-API，zero-jet中定义的 API
     * 5. JET-JOB, zero-jet中定义的 JOB
     */
    `TYPE`           VARCHAR(64) COMMENT '「type」- 类型保留，单独区分',
    `MAVEN_AID`      VARCHAR(255) COMMENT '「mavenAid」- 所在项目ID',
    `MAVEN_GID`      VARCHAR(255) COMMENT '「mavenGid」- 所在Group的ID',

    `SPEC_INTERFACE` VARCHAR(255) COMMENT '「specInterface」- 接口名称',
    `SPEC_IMPL`      VARCHAR(255) COMMENT '「specImpl」- 实现组件',
    `INTEGRATED`     BIT         DEFAULT NULL COMMENT '「integrated」- 是否用于外部集成',

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:b-component-2
ALTER TABLE B_COMPONENT
    ADD UNIQUE (`SPEC_IMPL`, `BLOCK_ID`);