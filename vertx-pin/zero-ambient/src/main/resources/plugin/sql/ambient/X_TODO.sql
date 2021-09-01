-- liquibase formatted sql

-- changeset Lang:ox-todo-1
-- 应用程序表：X_TODO
DROP TABLE IF EXISTS X_TODO;
CREATE TABLE IF NOT EXISTS X_TODO
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 待办主键',
    `SERIAL`         VARCHAR(255) COMMENT '「serial」- 待办编号，使用 X_NUMBER 生成',
    `NAME`           VARCHAR(255) COMMENT '「name」- 待办名称（标题）',
    `CODE`           VARCHAR(36) COMMENT '「code」- 待办系统码',
    `ICON`           VARCHAR(255) COMMENT '「icon」- 待办显示的图标',
    -- 待办相关内容
    /*
     * 待办状态清单：
     * PENDING：新生成的待办，等待办理
     * ACCEPTED：待办接收完成（长时间任务）
     * FINISHED：待办已完成，待确认变更已确认（相当于关闭）
     * REJECT：待办被退回
     * CANCEL：待办被取消
     */
    `STATUS`         VARCHAR(36) COMMENT '「status」- 待办状态',
    `TODO_URL`       VARCHAR(255) COMMENT '「todoUrl」- 待办路径',
    /*
     * EXPIRED：会超时的待办
     * STANDARD：标准的待办（不会超时）
     */
    `TYPE`           VARCHAR(36) COMMENT '「type」- 待办类型',
    `EXPIRED_AT`     DATETIME COMMENT '「expiredAt」- 超时时间',

    -- 模块相关 Join
    `MODEL_ID`       VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`      VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY` VARCHAR(36) COMMENT '「modelCategory」- 关联的category记录，只包含叶节点',

    /*
     * 完整流程：
     * 1）创建待办，填充 toGroup，特殊的填充 toUser，状态 PENDING
     * - 系统创建
     * - 人工创建，如果是人工待办，那么 assignedBy 则指定创建人，同 createdBy
     * 2）待办接收：填充 acceptedBy
     * 3) 待办完成：填充 finishedBy
     */
    `TO_GROUP`       VARCHAR(36) COMMENT '「toGroup」- 待办指定组',
    `TO_USER`        VARCHAR(36) COMMENT '「toUser」- 待办指定人',
    `TO_ROLE`        VARCHAR(36) COMMENT '「toRole」- 待办角色（集体）',
    `ASSIGNED_BY`    VARCHAR(36) COMMENT '「assignedBy」- 待办指派人',
    `ACCEPTED_BY`    VARCHAR(36) COMMENT '「acceptedBy」- 待办接收人',
    `FINISHED_BY`    VARCHAR(36) COMMENT '「finishedBy」- 待办完成人',
    `TRACE_ID`       VARCHAR(36) COMMENT '「traceId」- 同一个流程的待办执行分组',
    /*
     * 分离配置项待办和关系待办，形成不同类型
     */
    `PARENT_ID`      VARCHAR(36) COMMENT '「parentId」- 待办支持父子集结构，父待办执行时候子待办同样执行',

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-todo-2
-- Unique JsonKeys：独立唯一键定义
ALTER TABLE X_TODO
    ADD UNIQUE (`SIGMA`, `CODE`) USING BTREE;
ALTER TABLE X_TODO
    ADD UNIQUE (`SIGMA`, `SERIAL`) USING BTREE;

ALTER TABLE X_TODO
    ADD INDEX IDXM_X_TODO_SIGMA_STATUS (`SIGMA`, `STATUS`) USING BTREE;
ALTER TABLE X_TODO
    ADD INDEX IDXM_X_TODO_SIGMA_TYPE_STATUS (`SIGMA`, `STATUS`, `TYPE`) USING BTREE;