-- liquibase formatted sql

-- changeset Lang:ox-op-1
-- 应用程序使用的模板：UI_OP
DROP TABLE IF EXISTS UI_OP;
CREATE TABLE IF NOT EXISTS UI_OP
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 操作主键',
    `ACTION`     VARCHAR(255) COMMENT '「action」- S_ACTION中的code（权限检查专用）',
    `TEXT`       VARCHAR(255) COMMENT '「text」- 该操作上的文字信息',
    `EVENT`      VARCHAR(32) COMMENT '「event」- 操作中的 event 事件名称',
    `CLIENT_KEY` VARCHAR(32) COMMENT '「clientKey」- 一般是Html中对应的key信息，如 $opSave',
    `CLIENT_ID`  VARCHAR(32) COMMENT '「clientId」- 没有特殊情况，clientId = clientKey',

    -- 配置数据
    `CONFIG`     TEXT COMMENT '「config」- 该按钮操作对应的配置数据信息, icon, type',
    `PLUGIN`     TEXT COMMENT '「plugin」- 该按钮中的插件，如 tooltip，component等',

    -- 直接从 control 这个级别处理 OP
    /*
     * 1）列表类：CONTROL - LIST - COLUMN - OP（列表ID）
     * 2）表单类：CONTROL - FORM - FIELD - OP（表单ID）
     * 3）控件类：CONTROL - OP（控件ID）
     */
    `CONTROL_ID` VARCHAR(36) COMMENT '「controlId」- 挂载专用的ID',

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
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-op-2
ALTER TABLE UI_OP
    ADD UNIQUE (`CONTROL_ID`, `SIGMA`, `CLIENT_KEY`);
ALTER TABLE UI_OP
    ADD UNIQUE (`CONTROL_ID`, `SIGMA`, `ACTION`);

ALTER TABLE UI_OP
    ADD INDEX IDXM_UI_OP_SIGMA_CONTROL_ID (`SIGMA`,`CONTROL_ID`);