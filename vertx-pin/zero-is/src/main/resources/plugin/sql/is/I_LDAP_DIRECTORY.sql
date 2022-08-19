-- liquibase formatted sql

-- changeset Lang:ox-o_ldap-directory-1
-- OUserDirectory表：I_LDAP_DIRECTORY
-- 权限管理专用的用户目录表，和集成直接对接来实现用户目录的核心功能
DROP TABLE IF EXISTS I_LDAP_DIRECTORY;
CREATE TABLE IF NOT EXISTS I_LDAP_DIRECTORY
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 目录专用ID',
    `NAME`           VARCHAR(255) COMMENT '「name」- 目录名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 目录系统编码',
    /*
     * 目录的关联，使用
     * OWNER_TYPE + OWNER 和用户 / 用户组执行关联
     * 如果拥有集成ID，则消费集成配置，否则不消费集成配置执行关联
     */
    `OWNER_TYPE`     VARCHAR(64) COMMENT '「ownerType」- 关联主体类型',
    `OWNER`          VARCHAR(36) COMMENT '「owner」- 关联主体主键',

    `INTEGRATION_ID` VARCHAR(36) COMMENT '「integrationId」- 是否关联集成配置，管理时直接同步',
    `RUN_COMPONENT`  TEXT COMMENT '「runComponent」- 执行组件，LDAP执行专用',
    `RUN_CONFIG`     LONGTEXT COMMENT '「runConfig」- 执行组件额外配置',
    /*
     * secure = true，直接走集成配置
     * Integration
     *      - endpoint -> ldapUrl
     *      - path     -> ldapBase
     *      - username -> ldapUser
     *      - password -> ldapSecret
     * secure = false（简易处理）
     *      - ldapUrl
     *      - ldapBase
     *      - ldapUser
     *      - ldapSecret
     */
    `SECURE`         BIT COMMENT '「secure」- 是否启用安全协议，启用安全协议必走集成',
    /*
     * 配置结构字段值
     * [
     *      "ldapUrl",
     *      "ldapBase",
     *      "ldapUser",
     *      "ldapSecret"
     * ]
     */
    `LDAP_CONFIG`    TEXT COMMENT '「ldapConfig」- 如果不和集成相关，则直接存储配置信息',
    `LDAP_SOURCE`    LONGTEXT COMMENT '「ldapSource」- 绑定好过后，导入数据专用',

    -- 特殊属性
    `APP_ID`         VARCHAR(36) COMMENT '「appId」- 关联的应用程序ID',

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
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:ox-o_ldap-directory-2
ALTER TABLE I_LDAP_DIRECTORY
    ADD UNIQUE (`CODE`, `SIGMA`);