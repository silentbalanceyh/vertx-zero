-- liquibase formatted sql

-- changeset Lang:e-job-log-1
-- 邮件服务器专用表：E_JOB_LOG
DROP TABLE IF EXISTS E_JOB_LOG;
CREATE TABLE IF NOT EXISTS E_JOB_LOG
(
    `KEY`        VARCHAR(36) NOT NULL COMMENT '「key」- 记录主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 记录名称',
    `SERIAL`     VARCHAR(255) COMMENT '「serial」- 记录序号',

    -- 任务记录
    `TYPE`       VARCHAR(36) COMMENT '「type」- 记录类型：考勤 / 培训 / 休假 / 驻场等',
    `COMMENT`    TEXT COMMENT '「comment」- 记录备注',
    `LOG_AT`     DATETIME COMMENT '「logAt」- 记录时间',
    `LOG_BY`     VARCHAR(36) COMMENT '「logBy」- 记录人（只能自己记录，所以记录人就是所属人）',
    `LOG_NAME`   VARCHAR(255) COMMENT '「logName」- 记录人姓名',

    -- 关联信息
    `TODO_ID`    VARCHAR(36) COMMENT '「todoId」- 所属Todo主键，关联到主任务！',
    `COMPANY_ID` VARCHAR(36) COMMENT '「companyId」- 所属公司',

    -- 审批时使用
    `STATUS`     VARCHAR(36) COMMENT '「status」- 记录状态',

    -- 特殊字段
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);