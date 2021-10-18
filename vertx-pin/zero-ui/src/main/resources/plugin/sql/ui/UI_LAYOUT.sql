-- liquibase formatted sql

-- changeset Lang:ox-layout-1
-- 应用程序使用的模板：UI_LAYOUT
DROP TABLE IF EXISTS UI_LAYOUT;
CREATE TABLE IF NOT EXISTS UI_LAYOUT
(
    `KEY`        VARCHAR(36) COMMENT '「key」- Tpl模板唯一主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- Tpl模板名称',
    /*
     * 模板的配置信息只有一套
     * 1）每个 sigma 下边的 NAME 模板是一致的
     * 2）每个模板只有一个 config 的配置值
     */
    `CONFIG`     TEXT COMMENT '「config」- 主配置',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- 模板连接 Slice 块依靠的是配置程序中的位置数据，并不在表上做直接关联
-- uiData 负责渲染界面专用，metadata 负责配置程序
-- changeset Lang:ox-layout-2
ALTER TABLE UI_LAYOUT
    ADD UNIQUE (`NAME`, `SIGMA`) USING BTREE;