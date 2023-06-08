-- liquibase formatted sql

-- changeset Lang:b-authority-1
/*
 * BLOCK 中的资源定义完整记录
 * S_ACTION：安全操作记录
 * S_RESOURCE：和资源绑定的记录
 * S_PERMISSION：权限信息
 * S_VIEW：视图信息
 * （管理端）
 */
DROP TABLE IF EXISTS B_AUTHORITY;
CREATE TABLE IF NOT EXISTS B_AUTHORITY
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 主键',
    `CODE`           VARCHAR(255) COMMENT '「name」- 系统内部编码', -- TYPE + BLOCK_CODE
    `BLOCK_ID`       VARCHAR(36) COMMENT '「blockId」- 所属模块ID',

    /*
     * 这部分的区分
     * - CORE, 核心资源（基本CRUD）
     * - ASSIST，辅助资源（下拉、字典等）
     * - CHILD，子操作资源
     * - DEFINED，自定义资源（开发的新资源信息）
     * 每一个 BLOCK 一定有 CORE，另外三个可选
     */
    `TYPE`           VARCHAR(64) COMMENT '「type」- 类型保留，单独区分',

    -- 只针对 code 字段
    `LIC_RESOURCE`   LONGTEXT COMMENT '「licResource」- 资源编码',
    `LIC_ACTION`     LONGTEXT COMMENT '「licAction」- 操作编码',
    `LIC_PERMISSION` LONGTEXT COMMENT '「licPermission」- 所需权限集合',
    `LIC_VIEW`       LONGTEXT COMMENT '「licView」- 视图集合',

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:b-authority-2
ALTER TABLE B_AUTHORITY
    ADD UNIQUE (`CODE`, `BLOCK_ID`);