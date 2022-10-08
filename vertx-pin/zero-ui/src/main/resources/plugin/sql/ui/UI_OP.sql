-- liquibase formatted sql

-- changeset Lang:ox-op-1
-- 应用程序使用的模板：UI_OP
DROP TABLE IF EXISTS UI_OP;
CREATE TABLE IF NOT EXISTS UI_OP
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 操作主键',
    `ACTION`       VARCHAR(255) COMMENT '「action」- S_ACTION中的code（权限检查专用）',
    `TEXT`         VARCHAR(255) COMMENT '「text」- 该操作上的文字信息',
    `EVENT`        VARCHAR(128) COMMENT '「event」- 操作中的 event 事件名称',
    `CLIENT_KEY`   VARCHAR(128) COMMENT '「clientKey」- 一般是Html中对应的key信息，如 $opSave',
    `CLIENT_ID`    VARCHAR(128) COMMENT '「clientId」- 没有特殊情况，clientId = clientKey',

    -- 配置数据
    `CONFIG`       TEXT COMMENT '「config」- 该按钮操作对应的配置数据信息, icon, type',
    `PLUGIN`       TEXT COMMENT '「plugin」- 该按钮中的插件，如 tooltip，component等',

    -- 管理专用属性
    `UI_SORT`      INT COMMENT '「uiSort」- 按钮在管理过程中的排序',
    -- 直接从 control 这个级别处理 OP
    /*
     * 1）列表类：CONTROL - LIST - COLUMN - OP（列表ID）
     * 2）表单类：CONTROL - FORM - FIELD - OP（表单ID）
     * 3）控件类：CONTROL - OP（控件ID）
     * controlId 可区分维度，为了兼容动态模型、静态模型、流程模型，此处的操作可重构如下
     * 扩展 controlId 长度为 128，可存储某些专用的 code 值
     * -- LIST/FORM 动态类（zero-atom）中绑定的操作读取（controlType = null OR controlType = ATOM）
     *    此时必须属性：
     *    -- event,事件名称
     *    -- clientId,clientKey, 一般是同样的值
     *    -- controlId,对应的 UI_CONTROL 的主键
     * -- FLOW 类型（zero-wf）中绑定的流程表单专用操作（controlType = WORKFLOW）
     *    此时必须属性：
     *    -- event,task任务节点名称
     *    -- controlId,对应的 W_FLOW 中流程名称
     *    -- clientId,clientKey, 一般是同样的值
     * -- FREEDOM 类型，绑定的自由控件，此时（controlType = WEB）
     */
    `CONTROL_ID`   VARCHAR(128) COMMENT '「controlId」- 挂载专用的ID',
    `CONTROL_TYPE` VARCHAR(255) COMMENT '「controlType」- 操作关联的控件类型',

    -- 特殊字段
    `ACTIVE`       BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`        VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`     VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-op-2
ALTER TABLE UI_OP
    ADD UNIQUE (`CONTROL_ID`, `SIGMA`, `CLIENT_KEY`) USING BTREE;
ALTER TABLE UI_OP
    ADD UNIQUE (`CONTROL_ID`, `SIGMA`, `ACTION`) USING BTREE;

ALTER TABLE UI_OP
    ADD INDEX IDXM_UI_OP_SIGMA_CONTROL_ID (`SIGMA`, `CONTROL_ID`) USING BTREE;
ALTER TABLE UI_OP
    ADD INDEX IDXM_UI_OP_SIGMA_CONTROL_TYPE (`SIGMA`, `CONTROL_TYPE`);