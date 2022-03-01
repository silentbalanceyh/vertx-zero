-- liquibase formatted sql

-- changeset Lang:b-web-1
/*
 * BLOCK 中的界面资源定义（按页面分）
 * - UI_LIST, UI_COLUMN（包括静态）
 * - UI_FORM, UI_FIELD（包括静态）
 * - UI_CONTROL（包括静态）
 * （管理端）
 */
DROP TABLE IF EXISTS B_WEB;
CREATE TABLE IF NOT EXISTS B_WEB
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 主键',
    `CODE`        VARCHAR(255) COMMENT '「code」- 系统内部编码', -- TYPE + BLOCK_CODE
    `BLOCK_ID`    VARCHAR(36) COMMENT '「blockId」- 所属模块ID',

    /*
     * 这部分的区分
     * - LIST, 列表资源
     * - FORM，表单资源
     * - CONTROL，通用资源
     */
    `TYPE`        VARCHAR(64) COMMENT '「type」- 类型保留，单独区分',

    -- 只针对 code 字段
    `LIC_CONTENT` LONGTEXT COMMENT '「licContent」- 内容编码',
    `LIC_OP`      LONGTEXT COMMENT '「licOp」- 界面操作',
    `LIC_MODULE`  LONGTEXT COMMENT '「licModule」- 单独指定 X_MODULE 中的记录',
    `LIC_TPL`     LONGTEXT COMMENT '「licTpl」- PAGE, LAYOUT, CONTROL 等记录',

    -- 特殊字段
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:b-web-2
ALTER TABLE B_WEB
    ADD UNIQUE (`CODE`, `BLOCK_ID`);