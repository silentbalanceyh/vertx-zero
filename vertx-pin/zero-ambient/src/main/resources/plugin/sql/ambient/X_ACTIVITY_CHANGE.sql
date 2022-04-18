-- liquibase formatted sql

-- changeset Lang:ox-activity-change-1
-- 变更记录表：X_ACTIVITY_CHANGE
DROP TABLE IF EXISTS X_ACTIVITY_CHANGE;
CREATE TABLE IF NOT EXISTS X_ACTIVITY_CHANGE
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 操作行为主键',
    `ACTIVITY_ID` VARCHAR(36) COMMENT '「activityId」- 关联的操作ID',

    -- 是否变更记录
    `TYPE`        VARCHAR(64) COMMENT '「type」- 字段变更类型：ADD | DELETE | UPDATE 三种',
    `STATUS`      VARCHAR(64) COMMENT '「status」- 待确认变更状态：CONFIRMED | PENDING',

    `FIELD_NAME`  VARCHAR(255) COMMENT '「fieldName」- 如果是变更记录则需要生成变更日志',
    `FIELD_ALIAS` VARCHAR(255) COMMENT '「fieldAlias」- 字段对应的别名',
    `FIELD_TYPE`  VARCHAR(255) COMMENT '「fieldType」- 变更字段的数据类型，直接从模型定义中读取',
    `VALUE_OLD`   LONGTEXT COMMENT '「valueOld」- 旧值',
    `VALUE_NEW`   LONGTEXT COMMENT '「valueNew」- 新值',

    `SIGMA`       VARCHAR(128) COMMENT '「sigma」- 用户组绑定的统一标识',
    `LANGUAGE`    VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`      BIT COMMENT '「active」- 是否启用',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
ALTER TABLE X_ACTIVITY_CHANGE
    ADD INDEX IDX_X_ACTIVITY_CHANGE_ACTIVITY_ID (`ACTIVITY_ID`) USING BTREE;
ALTER TABLE X_ACTIVITY_CHANGE
    ADD INDEX IDX_X_ACTIVITY_CHANGE_CREATED_AT (`CREATED_AT`) USING BTREE;