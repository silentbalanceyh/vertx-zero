-- liquibase formatted sql

-- changeset Lang:x-linkage-1
DROP TABLE IF EXISTS `X_LINKAGE`;
CREATE TABLE `X_LINKAGE`
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 连接主键',
    `NAME`        VARCHAR(255) NOT NULL COMMENT '「name」- 名称',
    `TYPE`        VARCHAR(64)  NOT NULL COMMENT '「type」- 连接类型',
    `ALIAS`       VARCHAR(128) COMMENT '「alias」- 别称',
    `REGION`      VARCHAR(255) COMMENT '「region」- 连接区域标识，同一个区域算一个连接（批次）',

    -- 关联信息（主数据）
    -- 1）单向，LINK_KEY = Ordered ( SOURCE_KEY + TARGET_KEY )
    -- 2）双向，LINK_KEY = Non-Ordered ( SOURCE_KEY + TARGET_KEY, TARGET_KEY + SOURCE_KEY )
    `LINK_KEY`    VARCHAR(255) COMMENT '「linkKey」- 双向Key计算，根据 source / target 计算',
    `LINK_TYPE`   VARCHAR(255) COMMENT '「linkType」- 关系记录类型',
    `LINK_DATA`   LONGTEXT COMMENT '「linkData」- 关联数据Json格式',

    -- 源和目标
    `SOURCE_KEY`  VARCHAR(255) COMMENT '「sourceKey」- 源实体',
    `SOURCE_TYPE` VARCHAR(255) COMMENT '「sourceType」- 源实体类型',
    `SOURCE_DATA` LONGTEXT COMMENT '「sourceData」- 源记录Json格式',
    `TARGET_KEY`  VARCHAR(255) COMMENT '「targetKey」- 目标实体',
    `TARGET_TYPE` VARCHAR(255) COMMENT '「targetType」- 目标实体类型',
    `TARGET_DATA` LONGTEXT COMMENT '「targetData」- 目标记录Json格式',

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
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- changeset Lang:x-linkage-2
-- SourceKey + TargetKey = LinkKey
ALTER TABLE X_LINKAGE
    ADD UNIQUE (`LINK_KEY`);
-- Region + Name         = Link Unique on Business
ALTER TABLE X_LINKAGE
    ADD UNIQUE (`REGION`, `NAME`);

-- INDEX -- Fetch All
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_SIGMA (`SIGMA`, `TYPE`);
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_REGION (`REGION`);
-- INDEX -- FETCH BY TYPE
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_R_SOURCE_TYPE (`SOURCE_TYPE`);
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_R_TARGET_TYPE (`TARGET_TYPE`);
-- INDEX -- FETCH BY KEY
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_R_SOURCE_KEY (`SOURCE_KEY`);
ALTER TABLE X_LINKAGE
    ADD INDEX IDX_X_LINKAGE_R_TARGET_KEY (`TARGET_KEY`);