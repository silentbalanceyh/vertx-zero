-- liquibase formatted sql

-- changeset Lang:ox-activity-1
-- 应用程序表：X_ACTIVITY
DROP TABLE IF EXISTS X_ACTIVITY;
CREATE TABLE IF NOT EXISTS X_ACTIVITY
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 操作行为主键',
    /*
     * 变更记录专用字段处理：
     * - serial：                记录单号
     * - description：           操作描述信息
     * - taskName：              任务名称
     * - taskSerial：            任务单号
     * - createdAt：             操作时间
     * - createdBy：             操作人
     *
     * 应用级：
     * - sigma：                 统一标识符 sigma
     *
     * 模型级：
     * - type                    记录类型
     * - modelId：               模型统一标识符 identifier
     * - modelKey：              单据主键 key （流程节点中单号）
     * - modelCategory：         业务类别（可选保留）
     */
    `TYPE`           VARCHAR(64) COMMENT '「type」- 操作类型',
    `SERIAL`         VARCHAR(255) COMMENT '「serial」- 变更记录号',
    `DESCRIPTION`    TEXT COMMENT '「description」- 操作描述信息',

    -- 模块相关 Join
    `MODEL_ID`       VARCHAR(255) COMMENT '「modelId」- 组所关联的模型identifier，用于描述',
    `MODEL_KEY`      VARCHAR(36) COMMENT '「modelKey」- 组所关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY` VARCHAR(128) COMMENT '「modelCategory」- 关联的category记录，只包含叶节点',

    -- 流程专用
    `TASK_NAME`      VARCHAR(255) COMMENT '「taskName」- 任务名称',
    `TASK_SERIAL`    VARCHAR(255) COMMENT '「taskSerial」- 任务单号',

    -- 是否变更记录
    `RECORD_OLD`     LONGTEXT COMMENT '「recordOld」- 变更之前的数据（用于回滚）',
    `RECORD_NEW`     LONGTEXT COMMENT '「recordNew」- 变更之后的数据（用于更新）',

    `SIGMA`          VARCHAR(128) COMMENT '「sigma」- 用户组绑定的统一标识',
    `LANGUAGE`       VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`         BIT COMMENT '「active」- 是否启用',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:x-activity-2
ALTER TABLE X_ACTIVITY
    ADD INDEX IDXM_X_ACTIVITY_MODEL_ID_MODEL_KEY (`MODEL_ID`, `MODEL_KEY`, `ACTIVE`) USING BTREE;
ALTER TABLE X_ACTIVITY
    ADD INDEX IDXM_X_ACTIVITY_SIGMA_ACTIVE (`SIGMA`, `ACTIVE`) USING BTREE;
ALTER TABLE X_ACTIVITY
    ADD INDEX IDX_X_ACTIVITY_CREATED_AT (`CREATED_AT`) USING BTREE;

