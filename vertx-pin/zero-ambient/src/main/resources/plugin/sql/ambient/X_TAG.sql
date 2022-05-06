-- liquibase formatted sql

-- changeset Lang:x-tag-1
-- 标签专用表
DROP TABLE IF EXISTS X_TAG;
CREATE TABLE IF NOT EXISTS X_TAG
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 标签主键',
    `NAME`        VARCHAR(255) NOT NULL COMMENT '「name」- 标签名称',
    `TYPE`        VARCHAR(255) COMMENT '「type」- 标签类型',

    /*
     * 两种格式
     * tag:xxxxx
     * xxxxx
     */
    `ICON`        VARCHAR(255) COMMENT '「icon」- 标签使用的图标',
    `SORT`        BIGINT COMMENT '「sort」- 标签排序',
    `SHOW`        BIT         DEFAULT FALSE COMMENT '「show」- 是否显示在导航栏',
    `DESCRIPTION` LONGTEXT COMMENT '「description」- 标签描述',

    -- 所属应用
    `APP_ID`      VARCHAR(255) COMMENT '「appId」- 关联的应用程序ID',

    -- 特殊字段
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:x-tag-2
ALTER TABLE X_TAG
    ADD UNIQUE (`NAME`, `APP_ID`);