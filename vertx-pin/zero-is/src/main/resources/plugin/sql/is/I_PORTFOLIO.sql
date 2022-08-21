-- liquibase formatted sql

-- changeset Lang:ox-o_portfolio-1
-- IPortfolio表：I_PORTFOLIO
-- 文件夹，公事包，组合
DROP TABLE IF EXISTS I_PORTFOLIO;
CREATE TABLE IF NOT EXISTS I_PORTFOLIO
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 目录专用ID',
    `NAME`             VARCHAR(255) COMMENT '「name」- 目录名称',
    `CODE`             VARCHAR(255) COMMENT '「code」- 目录系统编码',

    /*
     * -- CREATED,   新创建
     * -- CONNECTED, 已连接（验证过）
     * -- EXPIRED,   更新后
     * -- FAILURE,   连接失败
     */
    `STATUS`           VARCHAR(255) COMMENT '「status」- 目录状态',
    -- 目录类型，此处目录类型和集成类型一致，如 ldap / ldap 标识 LDAP 集成类型和目录类型
    `TYPE`             VARCHAR(255) COMMENT '「type」- 目录类型',
    `INTEGRATION_ID`   VARCHAR(36) COMMENT '「integrationId」- 是否关联集成配置，管理时直接同步',

    /*
     * 目录的关联，使用
     * OWNER_TYPE + OWNER 执行关联
     * 如果拥有集成ID，则消费集成配置，否则不消费集成配置执行关联
     * -- OWNER_TYPE的值列表
     * -- COMPANY       -- E_COMPANY
     * -- CUSTOMER      -- E_CUSTOMER
     * -- DEPARTMENT    -- E_DEPT
     * -- TEAM          -- E_TEAM
     * -- GROUP         -- S_GROUP
     * -- ROLE          -- S_ROLE
     * -- USER          -- S_USER
     * LDAP处理流程
     * 1）一个用户可配置多个LDAP的 IPortfolio，对于它所绑定的集成部分可以是相同的也可以是不同的
     * 2）根据用户所配置的目录，可选择手动同步，如果是自动同步所有，则提取优先级比较高的数据作为核心数据
     * 3）I_PORTFOLIO 中存储的配置可提供子账号功能
     */
    `OWNER_TYPE`       VARCHAR(20) COMMENT '「ownerType」- 关联主体类型',
    `OWNER`            VARCHAR(36) COMMENT '「owner」- 关联主体主键',

    /* runComponent 会直接消费对应的几个字段中存储的配置值 */
    -- 执行组件专用处理，加载基础规则
    `RUN_COMPONENT`    TEXT COMMENT '「runComponent」- 执行组件，LDAP执行专用',
    `RUN_CONFIG`       LONGTEXT COMMENT '「runConfig」- 执行组件额外配置',

    -- 执行系统专用的数据内容
    `DATA_KEY`         NVARCHAR(512) COMMENT '「dataKey」- LDAP路径做完整标识',
    `DATA_CONFIG`      LONGTEXT COMMENT '「dataConfig」- 数据基础配置',
    `DATA_SECURE`      LONGTEXT COMMENT '「dataSecure」- 安全专用配置',
    `DATA_INTEGRATION` LONGTEXT COMMENT '「dataIntegration」- 绑定好过后，导入/导出数据专用配置',

    -- 特殊属性
    `APP_ID`           VARCHAR(36) COMMENT '「appId」- 关联的应用程序ID',

    -- 特殊字段
    `ACTIVE`           BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`            VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`         VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:ox-o_portfolio-2
ALTER TABLE I_PORTFOLIO
    ADD UNIQUE (`CODE`, `SIGMA`);