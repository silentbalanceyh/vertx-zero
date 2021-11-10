-- liquibase formatted sql

-- changeset Lang:w-todo-1
-- 应用程序表：W_TODO
DROP TABLE IF EXISTS W_TODO;
CREATE TABLE IF NOT EXISTS W_TODO
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 待办主键',
    `SERIAL`          VARCHAR(255) COMMENT '「serial」- 待办编号，使用 X_NUMBER 生成',
    `NAME`            VARCHAR(255) COMMENT '「name」- 待办名称（标题）',
    `CODE`            VARCHAR(36) COMMENT '「code」- 待办系统码',
    `ICON`            VARCHAR(255) COMMENT '「icon」- 待办显示的图标',
    -- 待办相关内容
    /*
     * 待办状态清单：
     * PENDING：新生成的待办，等待办理
     * ACCEPTED：待办接收完成（长时间任务）
     * FINISHED：待办已完成，待确认变更已确认（相当于关闭）
     * REJECT：待办被退回
     * CANCEL：待办被取消
     */
    `STATUS`          VARCHAR(36) COMMENT '「status」- 待办状态',
    `TODO_URL`        VARCHAR(255) COMMENT '「todoUrl」- 待办路径',
    /*
     * EXPIRED：会超时的待办
     * STANDARD：标准的待办（不会超时）
     */
    `TYPE`            VARCHAR(36) COMMENT '「type」- 待办类型',

    /*
     * 定义模型的详细信息，该内容属于最核心的关联任务
     * 1）动态建模
     * - MODEL_ID: DataAtom关联模型的identifier（和模型直接相关的待办）
     * 2）静态建模
     * - MODEL_DAO：直接关联到模型对应的Dao Class名称，用于关联静态建模信息
     * 3）流程建模
     * - INSTANCE_ID：关联的Camunda工作流相关信息
     * 所有模型通用
     * - MODEL_KEY：模型记录主键
     * - MODEL_CATEGORY：模型记录所属的分类记录（只关联叶节点）
     * - MODEL_FORM：当前待办关联表单（如果是流程建模则直接使用二次读取）
     */
    `MODEL_ID`        VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`       VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY`  VARCHAR(36) COMMENT '「modelCategory」- 关联的category记录，只包含叶节点',
    `MODEL_FORM`      VARCHAR(255) COMMENT '「modelForm」- 待办专用的表单关联',
    `MODEL_COMPONENT` VARCHAR(255) COMMENT '「modelComponent」- 关联的待办组件记录',

    `INSTANCE`        BIT         DEFAULT NULL COMMENT '「instance」- 是否启用工作流？',

    /*
     * 完整流程：
     * 1）创建待办，填充 toGroup，特殊的填充 toUser，状态 PENDING
     * - 系统创建
     * - 人工创建，如果是人工待办，那么 assignedBy 则指定创建人，同 createdBy
     * 2）待办接收：填充 acceptedBy
     * 3) 待办完成：填充 finishedBy
     */
    `TO_GROUP_MODE`   VARCHAR(32) COMMENT '「toGroupMode」- 部门、业务组、组、角色、地点等',
    `TO_GROUP`        VARCHAR(36) COMMENT '「toGroup」- 待办指定组',
    `TO_USER`         VARCHAR(36) COMMENT '「toUser」- 待办指定人',
    `TO_ROLE`         VARCHAR(36) COMMENT '「toRole」- 待办角色（集体）',

    /*
     * 分离配置项待办和关系待办，形成不同类型
     */
    `TRACE_ID`        VARCHAR(36) COMMENT '「traceId」- 同一个流程的待办执行分组',
    `PARENT_ID`       VARCHAR(36) COMMENT '「parentId」- 待办支持父子集结构，父待办执行时候子待办同样执行',

    -- 特殊字段
    `DESCRIPTION`     LONGTEXT COMMENT '「description」- 待办描述',
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- 指派字段
    `OWNER`           VARCHAR(36) COMMENT '「owner」- 拥有者',        -- 拥有者
    `SUPERVISOR`      VARCHAR(36) COMMENT '「supervisor」- 监督人',   -- 监督人
    `ASSIGNED_BY`     VARCHAR(36) COMMENT '「assignedBy」- 待办指派人', -- 指派人
    `ASSIGNED_AT`     DATETIME COMMENT '「assignedAt」- 指派时间',
    `ACCEPTED_BY`     VARCHAR(36) COMMENT '「acceptedBy」- 待办接收人', -- 接收人
    `ACCEPTED_AT`     DATETIME COMMENT '「acceptedAt」- 接收时间',
    `FINISHED_BY`     VARCHAR(36) COMMENT '「finishedBy」- 待办完成人', -- 完成人
    `FINISHED_AT`     DATETIME COMMENT '「finishedAt」- 完成时间',
    `EXPIRED_AT`      DATETIME COMMENT '「expiredAt」- 超时时间',
    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',    -- 创建人
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',    -- 更新人
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:w-todo-2
-- Unique JsonKeys：独立唯一键定义
ALTER TABLE W_TODO
    ADD UNIQUE (`SIGMA`, `CODE`) USING BTREE;
ALTER TABLE W_TODO
    ADD UNIQUE (`SIGMA`, `SERIAL`) USING BTREE;

ALTER TABLE W_TODO
    ADD INDEX IDXM_W_TODO_SIGMA_STATUS (`SIGMA`, `STATUS`) USING BTREE;
ALTER TABLE W_TODO
    ADD INDEX IDXM_W_TODO_SIGMA_TYPE_STATUS (`SIGMA`, `STATUS`, `TYPE`) USING BTREE;