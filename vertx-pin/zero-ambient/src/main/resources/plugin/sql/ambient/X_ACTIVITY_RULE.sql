-- liquibase formatted sql

-- changeset Lang:ox-activity-rule-1
-- 历史记录规则表：X_ACTIVITY_RULE
DROP TABLE IF EXISTS X_ACTIVITY_RULE;
CREATE TABLE IF NOT EXISTS X_ACTIVITY_RULE
(
    /*
     * type规则类型说明：
     * - WORKFLOW：          流程类（流程驱动Camunda）
     *       TYPE = 'WORKFLOW' AND DEFINITION_KEY = ? AND TASK_KEY = ?
             流程类为局部规则，只有流程移动时触发该类规则，其他情况不触发该规则。
     * - NOTIFICATION：      提醒类
     *       TYPE = 'NOTIFICATION' AND DEFINITION_KEY = ? AND TASK_KEY = ?
     *       提醒类为局部规则，不论字段是否发生改变，只要条件通过则直接触发该提醒。
     * - ATOM：              模型类
     *       TYPE = 'ATOM' AND DEFINITION_KEY = ?
     *       模型类为全局规则，字段发生变化时该规则生效，读取时直接根据流程定义key和类型读取。
     *
     * 规则查询注意：
     * 1) 查询模型类时，先根据 definitionKey （流程定义key）查询所有规则集
     * -- 类型可能多个       ( type = ? )
     * 2) 查询条件有二：
     * -- 直接查询全局规则：definitionKey = ? and taskKey = null
     * -- 查询当前节点局部规则：definitionKey = ? and taskKey = ?
     *
     * 触发流程
     * ATOM - ruleIdentifier, ruleField
     *      -- 当某个模型中对应的字段发生改变时生成最终消息
     * WORKFLOW - definitionKey, taskKey
     *      -- 当流程触发时生成最终信息
     * NOTIFICATION
     *      -- 这种模式下会触发提醒相关内容，直接启用 ruleConfig 中配置信息处理
     *
     * > 默认 ruleExpression 都为 true，即都会解析表达式，必须表达式满足时才执行，如果表达式为 NULL 则不执行。
     *
     * 条件判断
     * 1) 关联的模型
     * -- Todo模型
     * -- Ticket主单模型
     * -- Ticket Extension扩展子单模型
     * -- Record记录模型
     * 2) 关联模型的变化字段
     * 3) 关联模型的字段变化方向
     * -- ADD：有值
     * -- UPDATE：变化
     * -- CHANGE：变化值
     */
    `KEY`             VARCHAR(36) COMMENT '「key」- 规则主键',
    `DEFINITION_KEY`  VARCHAR(128) COMMENT '「definitionKey」- 流程对应的 definitionKey，用于查询所有规则用',
    `TASK_KEY`        VARCHAR(255) COMMENT '「taskKey」- 和待办绑定的taskKey',
    `TYPE`            VARCHAR(64) COMMENT '「type」- 规则类型',

    /*
     * 只有两种
     * 1) 第一种，直接：${taskName} 模式构造流程名称
     * 2) 第二种，直接设置 typeName 为流程名称
     */
    `RULE_NAME`       VARCHAR(255) COMMENT '「ruleName」- 规则名称，如果 type = ATOM 时读取，并设置到 typeName 中',
    `RULE_ORDER`      BIGINT COMMENT '「ruleOrder」- 规则触发顺序，修正两个时间戳，生成时序号统一，先生成的规则排序在上边',
    `RULE_NS`         VARCHAR(255) COMMENT '「ruleNs」- 规则所属主模型名空间',
    `RULE_IDENTIFIER` VARCHAR(255) COMMENT '「ruleIdentifier」- 主模型ID',
    `RULE_FIELD`      VARCHAR(128) COMMENT '「ruleField」- 主字段名',

    /*
     * Message generated
     * -- 1) evaluate expression
     * -- 2) message + tpl, message with tpl ( parameters ) output final message
     */
    `RULE_EXPRESSION` LONGTEXT COMMENT '「ruleExpression」- 规则触发表达式 ( 可以是多个，JsonArray格式 )',
    `RULE_TPL`        TEXT COMMENT '「ruleTpl」- 参数模板专用，JsonObject结构',
    `RULE_CONFIG`     LONGTEXT COMMENT '「ruleConfig」- 规则对应的额外配置',
    `RULE_MESSAGE`    TEXT COMMENT '「ruleMessage」- 输出消息专用, Ut.fromExpression解析（特殊解析）',

    /*
     * 回调组件（触发规则时的 After 专用组件）
     */
    `HOOK_COMPONENT`  VARCHAR(255) COMMENT '「hookComponent」-- 回调钩子组件',
    `HOOK_CONFIG`     LONGTEXT COMMENT '「hookConfig」-- 回调钩子组件配置',

    `LOGGING`         BIT         DEFAULT NULL COMMENT '「logging」- 是否记录日志',

    -- 特殊字段
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:x-activity-rule-2
ALTER TABLE X_ACTIVITY_RULE
    ADD INDEX IDXM_X_ACTIVITY_RULE_DEFINITION_TASK_KEY (`DEFINITION_KEY`, `TASK_KEY`, `SIGMA`);