-- liquibase formatted sql

-- changeset Lang:ox-api-1
-- 接口定义表：I_API
DROP TABLE IF EXISTS I_API;
CREATE TABLE IF NOT EXISTS I_API
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 接口ID',
    -- API 定义专用字段，用于设置路由
    `NAME`            VARCHAR(255) COMMENT '「name」- 接口名称',
    `URI`             VARCHAR(255) COMMENT '「uri」- 接口路径，安全路径位于 /api 之下',
    `METHOD`          VARCHAR(20) COMMENT '「method」- 接口对应的HTTP方法',
    `CONSUMES`        TEXT COMMENT '「consumes」- 当前接口使用的客户端 MIME',
    `PRODUCES`        TEXT COMMENT '「produces」- 当前接口使用的服务端 MIME',
    `SECURE`          BIT COMMENT '「secure」- 是否走安全通道，默认为TRUE',
    `COMMENT`         TEXT COMMENT '「comment」- 备注信息',

    -- API 模式和参数处理
    `TYPE`            VARCHAR(64) COMMENT '「type」- 通信类型，ONE-WAY / REQUEST-RESPONSE / PUBLISH-SUBSCRIBE',
    `PARAM_MODE`      VARCHAR(20) COMMENT '「paramMode」- 参数来源，QUERY / BODY / DEFINE / PATH',
    `PARAM_REQUIRED`  TEXT COMMENT '「paramRequired」- 必须参数表，一个JsonArray用于返回 400基本验证（验证Query和Path）',
    `PARAM_CONTAINED` TEXT COMMENT '「paramContained」- 必须参数表，一个JsonArray用于返回 400基本验证（验证Body）',

    /*
     * IN_RULE：Api的输入基本规则
     * IN_MAPPING：Api的映射规则
     * IN_PLUG：Api的中使用的插件信息
     * IN_SCRIPT：Api中的脚本引擎
     */
    `IN_RULE`         TEXT COMMENT '「inRule」- 参数验证、转换基本规则',
    `IN_MAPPING`      TEXT COMMENT '「inMapping」- 参数映射规则',
    `IN_PLUG`         VARCHAR(255) COMMENT '「inPlug」- 参数请求流程中的插件',
    `IN_SCRIPT`       VARCHAR(255) COMMENT '「inScript」- 【保留】参数请求流程中的脚本控制',
    /*
     * OUT_WRITER：响应专用的 Writer 处理响应格式
     */
    `OUT_WRITER`      VARCHAR(255) COMMENT '「outWriter」- 响应格式处理器',

    -- 服务层连接器
    `WORKER_TYPE`     VARCHAR(255) COMMENT '「workerType」- Worker类型：JS / PLUG / STD',
    `WORKER_ADDRESS`  VARCHAR(255) COMMENT '「workerAddress」- 请求发送地址',
    `WORKER_CONSUMER` VARCHAR(255) COMMENT '「workerConsumer」- 请求地址消费专用组件',
    `WORKER_CLASS`    VARCHAR(255) COMMENT '「workerClass」- OX | PLUG专用，请求执行器对应的JavaClass名称',
    `WORKER_JS`       VARCHAR(255) COMMENT '「workerJs」- JS 专用，JavaScript路径：runtime/workers/<app>/下的执行器',

    -- 响应格式处理
    `SERVICE_ID`      VARCHAR(36) COMMENT '「serviceId」- 关联的服务ID',

    -- 特殊字段
    `SIGMA`           VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`        VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`          BIT COMMENT '「active」- 是否启用',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-api-2
ALTER TABLE I_API
    ADD UNIQUE (`URI`, `METHOD`, `SIGMA`) USING BTREE;
