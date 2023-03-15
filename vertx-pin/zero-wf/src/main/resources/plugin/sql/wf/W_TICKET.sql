-- liquibase formatted sql

-- changeset Lang:w-ticket-1
-- 流程主单：W_TICKET
DROP TABLE IF EXISTS W_TICKET;
CREATE TABLE IF NOT EXISTS W_TICKET
(
    /*
     * 主单基本信息
     * - key，单据主键（自动生成，一般是UUID格式）
     * - serial，主单单号，直接通过 X_NUMBER 生成
     *   - TODO的单号根据主单单号 + 01,02,03的格式生成
     * - code，保留字段（系统内码）
     * - status（业务状态）
     * - type（单据类型，流程单据类型挂载到服务目录中，标识当前业务主单提供何种服务）
     */
    `KEY`                 VARCHAR(36) COMMENT '「key」- 单据主键',
    `SERIAL`              VARCHAR(255) COMMENT '「serial」- 单据编号，使用 X_NUMBER 生成',
    `NAME`                VARCHAR(255) COMMENT '「name」- 单据标题',
    `CODE`                VARCHAR(36) COMMENT '「code」- 单据系统编号（内码）',
    `TYPE`                VARCHAR(36) COMMENT '「type」- 主单类型类型',
    `PHASE`               VARCHAR(36) COMMENT '「phase」- 主单据所属阶段（状态描述，由于挂TODO，所以不使用status）',

    /*
     * Modeling Engine（建模管理）
     * 1）动态建模
     * -- MODEL_ID：关联模型的identifier
     * 2）静态模型
     * -- MODEL_COMPONENT：模型专用组件，DAO或动态模型专用组件
     * 3）数据详细信息
     * -- MODEL_KEY（记录专用主键）
     * -- MODEL_CATEGORY（关联的category记录）
     *
     *                  WTicket       T_??              Model
     * 文件管理             1           0                   1
     * 资产入库             1         1 x ASSET_IN          N
     * 资产出库             1         1 x ASSET_OUT         N
     * 资产报废             1         1 x ASSET_KO          N
     * -- MODEL_CHILD 只有在 Model = N 的时候使用，在 Model = N 时，还依赖另外一张 T_?? 的单据表，单据中
     *    存储了主单没有的特殊信息，此时MODEL_CHILD中存储的是相关实体的主键集，为JsonArray格式
     */
    `MODEL_ID`            VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`           VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY`      VARCHAR(128) COMMENT '「modelCategory」- 关联的category记录，只包含叶节点',
    `MODEL_COMPONENT`     VARCHAR(255) COMMENT '「modelComponent」- 关联的待办组件记录',
    /*
     * 批量专用字段
     */
    `MODEL_CHILD`         LONGTEXT COMMENT '「modelChild」- 关联多个模型的记录ID，JsonArray格式',
    `QUANTITY`            INTEGER COMMENT '「quantity」- 数量信息，多个模型记录时统计模型总数',


    /*
     * Camunda Workflow Engine
     * - 主单流程相关信息
     * - definitionKey, 流程定义专用Key，标识当前主单隶属于哪个流程
     * - processId,     流程执行专用Key，标识当前主单目前处于哪个流程节点专用
     * - finished,      （主单是否执行完成）
     */
    `FLOW_DEFINITION_KEY` VARCHAR(64) COMMENT '「flowDefinitionKey」- 流程定义的KEY, getProcessDefinitionKey',
    `FLOW_DEFINITION_ID`  VARCHAR(64) COMMENT '「flowDefinitionId」- 流程定义的ID，getProcessDefinitionKey',
    `FLOW_INSTANCE_ID`    VARCHAR(64) COMMENT '「flowInstanceId」- 流程定义的ID，getProcessId',
    `FLOW_END`            BIT         DEFAULT NULL COMMENT '「flowEnd」- 主单是否执行完成',


    /*
     * 流程核心业务信息（主要记录主单的相关信息）
     * - title：主单标题（用户自己填写）
     * - description：主单描述（用户自己填写）
     * - catalog：所属服务目录（绑定服务目录信息）
     */
    `TITLE`               VARCHAR(1024) COMMENT '「title」- 主单业务标题',
    `DESCRIPTION`         LONGTEXT COMMENT '「description」- 主单描述内容',
    `CATALOG`             VARCHAR(36) COMMENT '「catalog」- 关联服务目录',
    `CATEGORY`            VARCHAR(36) COMMENT '「category」- 业务类别',
    `CATEGORY_SUB`        VARCHAR(36) COMMENT '「categorySub」- 子类别',

    `OWNER`               VARCHAR(36) COMMENT '「owner」- 制单人/拥有者',
    `SUPERVISOR`          VARCHAR(36) COMMENT '「supervisor」- 监督人',

    /*
     * 主单核心人员信息
     * - open, 开单人（开单人和拥有者可以不同，帮别人开单）
     * - cancel, 终止人，有人直接中断该单据，则流程完结，直接设置该信息
     * - close, 关单人（谁完成该单据并关闭，如果有重开，则该值为最终关单人）
     */
    `OPEN_BY`             VARCHAR(36) COMMENT '「openBy」- 开单人',
    `OPEN_GROUP`          VARCHAR(36) COMMENT '「openGroup」- 开单组',
    `OPEN_AT`             DATETIME COMMENT '「openAt」- 开单时间',
    `CANCEL_BY`           VARCHAR(36) COMMENT '「cancelBy」- 中断人',
    `CANCEL_AT`           DATETIME COMMENT '「cancelAt」- 中断时间',

    `CLOSE_BY`            VARCHAR(36) COMMENT '「closeBy」- 关闭人',
    `CLOSE_AT`            DATETIME COMMENT '「closeAt」- 关闭时间',
    `CLOSE_SOLUTION`      LONGTEXT COMMENT '「closeSolution」- 关闭解决方案',
    `CLOSE_CODE`          VARCHAR(255) COMMENT '「closeCode」- 关闭代码',
    `CLOSE_KB`            VARCHAR(1024) COMMENT '「closeKb」- 关闭时KB链接地址',

    /*
     * 核心八个信息（Zero Framework自带）
     */
    `ACTIVE`              BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`               VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`            TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`            VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',
    -- Auditor字段
    `CREATED_AT`          DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`          VARCHAR(36) COMMENT '「createdBy」- 创建人', -- 创建人
    `UPDATED_AT`          DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`          VARCHAR(36) COMMENT '「updatedBy」- 更新人', -- 更新人
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-ticket-2
-- 流程主单：W_TICKET
ALTER TABLE W_TICKET
    ADD UNIQUE (`SIGMA`, `CODE`);
ALTER TABLE W_TICKET
    ADD UNIQUE (`SIGMA`, `SERIAL`);

-- 查询单一状态
ALTER TABLE W_TICKET
    ADD INDEX IDXM_W_TICKET_SIGMA_STATUS (`SIGMA`, `PHASE`);
-- 查询单一流程
ALTER TABLE W_TICKET
    ADD INDEX IDXM_W_TICKET_SIGMA_FLOW_DEFINITION_KEY (`SIGMA`, `FLOW_DEFINITION_KEY`);
-- 查询单一服务目录
ALTER TABLE W_TICKET
    ADD INDEX IDXM_W_TICKET_SIGMA_CATALOG (`SIGMA`, `CATALOG`);
-- 状态 + 类型
ALTER TABLE W_TICKET
    ADD INDEX IDXM_W_TICKET_SIGMA_TYPE_STATUS (`SIGMA`, `PHASE`, `TYPE`);