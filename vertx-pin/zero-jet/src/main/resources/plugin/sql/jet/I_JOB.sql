-- liquibase formatted sql

-- changeset Lang:ox-job-1
-- 任务定义表：I_SERVICE
DROP TABLE IF EXISTS I_JOB;
CREATE TABLE IF NOT EXISTS I_JOB
(
    `KEY`               VARCHAR(36) COMMENT '「key」- 任务ID',

    -- 名空间处理
    `NAMESPACE`         VARCHAR(255) COMMENT '「namespace」- 任务所在名空间',
    `NAME`              VARCHAR(255) COMMENT '「name」- 任务名称',
    `CODE`              VARCHAR(255) COMMENT '「comment」- 任务编码',

    -- 存储对应的数据
    `TYPE`              VARCHAR(20) COMMENT '「type」- 任务类型',
    `GROUP`             VARCHAR(64) COMMENT '「group」- 任务组（按组查询），自由字符串',
    `COMMENT`           TEXT COMMENT '「comment」- 备注信息',
    `ADDITIONAL`        TEXT COMMENT '「additional」- 额外配置信息',

    -- JOB基本配置（包括调用基本信息）
    `RUN_AT`            TIME COMMENT '「runAt」- 定时任务中的JOB时间',
    `DURATION`          BIGINT COMMENT '「duration」- JOB的间隔时间，（秒为单位）',
    `PROXY`             VARCHAR(255) COMMENT '「proxy」- 代理类，带有@On/@Off',
    `THRESHOLD`         INT COMMENT '「threshold」- 默认值 300 s，（秒为单位）',
    /*
     * JOB的出入配置，优先级：
     * 1. 配置优先（也就是存在这四个字段中的值优先）
     * 2. 配置之后是注解（当这四个字段中没有值的时候读取注解）
     * 3. 注解也拿不到则不处理
     * 但是，对于 Code 模式（编程模式）下这四个字段，@Job -> config 会优先
     */
    `INCOME_COMPONENT`  VARCHAR(255) COMMENT '「incomeComponent」对应income，必须是JobIncome，@On -> income',
    `INCOME_ADDRESS`    VARCHAR(255) COMMENT '「incomeAddress」对应incomeAddress，字符串，@On -> address',
    `OUTCOME_COMPONENT` VARCHAR(255) COMMENT '「outcomeComponent」对应outcome，必须是JobOutcome，@Off -> outcome',
    `OUTCOME_ADDRESS`   VARCHAR(255) COMMENT '「outcomeAddress」对应outcomeAddress，字符串，@Off -> address',

    -- TASK关联的ServiceID，后续执行Service专用，不关联则提供单独的TASK实现
    `SERVICE_ID`        VARCHAR(36) COMMENT '「serviceId」- 关联的服务ID',

    -- 特殊字段
    `SIGMA`             VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`          VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`            BIT COMMENT '「active」- 是否启用',
    `METADATA`          TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`        DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`        VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`        DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`        VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:ox-job-2
ALTER TABLE I_JOB
    ADD UNIQUE (`SIGMA`, `CODE`);
ALTER TABLE I_JOB
    ADD UNIQUE (`SIGMA`, `NAME`);
ALTER TABLE I_JOB
    ADD UNIQUE (`NAMESPACE`, `NAME`);