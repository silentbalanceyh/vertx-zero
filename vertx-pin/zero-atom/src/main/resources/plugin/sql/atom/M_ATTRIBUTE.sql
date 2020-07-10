-- liquibase formatted sql

-- changeset Lang:ox-attribute-1
-- 模型属性表：M_ATTRIBUTE
DROP TABLE IF EXISTS M_ATTRIBUTE;
CREATE TABLE IF NOT EXISTS M_ATTRIBUTE
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 属性ID',
    `NAME`          VARCHAR(255) COMMENT '「name」- 属性名称',
    `ALIAS`         VARCHAR(255) COMMENT '「alias」- 属性别名（业务名）',
    `TYPE`          VARCHAR(10) COMMENT '「type」- INTERNAL/EXTERNAL属性',
    `SOURCE`        VARCHAR(255) COMMENT '「source」- 关联实体ID',
    `SOURCE_FIELD`  VARCHAR(255) COMMENT '「sourceField」- 可选，如果不设置则以name为主',
    `EXPRESSION`    TEXT COMMENT '「expression」- 表达式',
    `NORMALIZE`     TEXT COMMENT '「normalize」- 表达式',
    `IN_COMPONENT`  VARCHAR(255) COMMENT '「inComponent」- 写入插件',
    `OUT_COMPONENT` VARCHAR(255) COMMENT '「outComponent」- 读取插件',
    `MODEL_ID`      VARCHAR(36) COMMENT '「modelId」- 关联的模型ID',
    `COMMENTS`      TEXT COMMENT '「comments」- 当前属性的描述信息',

    -- 标记
    `IS_ARRAY`      BIT COMMENT '「isArray」- 是否集合属性，集合属性在导入导出时可用（保留）',
    `IS_SYNC_IN`    BIT COMMENT '「isSyncIn」- 是否同步读',
    `IS_SYNC_OUT`   BIT COMMENT '「isSyncOut」- 是否同步写',
    `IS_LOCK`       BIT COMMENT '「isLock」- 是否锁定，锁定属性不可删除',
    `IS_TRACK`      BIT COMMENT '「isTrack」- 是否实现历史记录，如果是 isTrack 那么启用 ACTIVITY 的变更记录，对应 ITEM',
    `IS_CONFIRM`    BIT COMMENT '「isConfirm」- 是否生成待确认变更，只有放在待确认变更中的数据需要生成待确认变更',

    -- 特殊字段
    `SIGMA`         VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-attribute-2
ALTER TABLE M_ATTRIBUTE
    ADD UNIQUE (`NAME`, `MODEL_ID`);

ALTER TABLE M_ATTRIBUTE
    ADD INDEX IDX_M_ATTRIBUTE_MODEL_ID (`MODEL_ID`);