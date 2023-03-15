-- liquibase formatted sql

-- changeset Lang:ox-job-1
-- 任务定义表：I_SERVICE
DROP TABLE IF EXISTS I_JOB;
CREATE TABLE IF NOT EXISTS I_JOB
(
    `KEY`                VARCHAR(36) COMMENT '「key」- 任务ID',

    -- 名空间处理
    `NAMESPACE`          VARCHAR(255) COMMENT '「namespace」- 任务所在名空间',
    `NAME`               VARCHAR(255) COMMENT '「name」- 任务名称',
    `CODE`               VARCHAR(255) COMMENT '「comment」- 任务编码',

    -- 存储对应的数据
    `GROUP`              VARCHAR(64) COMMENT '「group」- 任务组（按组查询），自由字符串',
    `COMMENT`            TEXT COMMENT '「comment」- 备注信息',
    `ADDITIONAL`         TEXT COMMENT '「additional」- 额外配置信息',

    -- JOB基本配置（包括调用基本信息）
    `PROXY`              VARCHAR(255) COMMENT '「proxy」- 代理类，带有@On/@Off',
    `THRESHOLD`          INT COMMENT '「threshold」- 默认值 300 s，（秒为单位）',

    -- 复杂调度
    /*
     * 1. 调度任务类型
     *    FIXED | ONCE | FORMULA
     * 2. runAt（第一次执行时间，起点）
     * 3. runFormula 表达式（编程模式中会直接解析），必须是 FORMULA（每个表达式后边支持多个值，比如周一，周三，周五）
     * -- 每天执行：D,时间点1,时间点2,....
     *    -- D,00:12,....
     * -- 每周执行：W,时间点1,时间点2,....
     *    -- W,00:12/3,....  3 表示周三（第3天）
     * -- 每月执行：M,时间点1,时间点2,....
     *    -- M,00:12/4,....  4 表示4号（第4天）
     * -- 每季执行：Q,时间点1,时间点2,....
     *    -- Q,00:12/33,.... 33 表示该季度 第33天
     *    -- Q,00:12/2-4,.... 2-4 表示该季度 第2个月第2天
     * -- 每年执行：Y,时间点1,时间点2,....
     *    -- Y,00:12/2-22,.... 2-22 表示该年 2月22日（第53天）
     *    -- Y,00:12/55,....   55 表示该年 第55天
     */
    `TYPE`               VARCHAR(20) COMMENT '「type」- 任务类型',
    `RUN_AT`             TIME COMMENT '「runAt」- 定时任务中的JOB时间',
    `RUN_FORMULA`        TEXT COMMENT '「runFormula」- 运行周期专用的表达式',
    `DURATION`           BIGINT COMMENT '「duration」- JOB的间隔时间，（秒为单位）',

    `DURATION_COMPONENT` VARCHAR(255) COMMENT '「durationComponent」对应复杂调度问题',
    `DURATION_CONFIG`    LONGTEXT COMMENT '「durationConfig」复杂调度配置',

    /*
     * JOB的出入配置，优先级：
     * 1. 配置优先（也就是存在这四个字段中的值优先）
     * 2. 配置之后是注解（当这四个字段中没有值的时候读取注解）
     * 3. 注解也拿不到则不处理
     * 但是，对于 Code 模式（编程模式）下这四个字段，@Job -> config 会优先
     */
    `INCOME_COMPONENT`   VARCHAR(255) COMMENT '「incomeComponent」对应income，必须是JobIncome，@On -> input',
    `INCOME_ADDRESS`     VARCHAR(255) COMMENT '「incomeAddress」对应incomeAddress，字符串，@On -> address',
    `OUTCOME_COMPONENT`  VARCHAR(255) COMMENT '「outcomeComponent」对应outcome，必须是JobOutcome，@Off -> outcome',
    `OUTCOME_ADDRESS`    VARCHAR(255) COMMENT '「outcomeAddress」对应outcomeAddress，字符串，@Off -> address',

    -- TASK关联的ServiceID，后续执行Service专用，不关联则提供单独的TASK实现
    `SERVICE_ID`         VARCHAR(36) COMMENT '「serviceId」- 关联的服务ID',

    -- 特殊字段
    `SIGMA`              VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`           VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`             BIT COMMENT '「active」- 是否启用',
    `METADATA`           TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`         DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`         VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`         DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`         VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:ox-job-2
ALTER TABLE I_JOB
    ADD UNIQUE (`SIGMA`, `CODE`) USING BTREE;
ALTER TABLE I_JOB
    ADD UNIQUE (`SIGMA`, `NAME`) USING BTREE;
ALTER TABLE I_JOB
    ADD UNIQUE (`NAMESPACE`, `NAME`) USING BTREE;

-- 用 sigma 查询
ALTER TABLE I_JOB
    ADD INDEX IDX_I_JOB_SIGMA (`SIGMA`) USING BTREE;
ALTER TABLE I_JOB
    ADD INDEX IDX_I_JOB_SERVICE_ID (`SERVICE_ID`) USING BTREE;
ALTER TABLE I_JOB
    ADD INDEX IDXM_I_JOB_GROUP_SIGMA (`SIGMA`, `GROUP`) USING BTREE;
ALTER TABLE I_JOB
    ADD INDEX IDXM_I_JOB_TYPE_SIGMA (`SIGMA`, `TYPE`) USING BTREE;
ALTER TABLE I_JOB
    ADD INDEX IDXM_I_JOB_GROUP_TYPE_SIGMA (`SIGMA`, `GROUP`, `TYPE`) USING BTREE;