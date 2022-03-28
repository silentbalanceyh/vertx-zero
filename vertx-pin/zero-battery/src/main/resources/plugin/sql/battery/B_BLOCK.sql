-- liquibase formatted sql

-- changeset Lang:b-block-1
DROP TABLE IF EXISTS B_BLOCK;
CREATE TABLE IF NOT EXISTS B_BLOCK
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 模块主键',
    `CODE`           VARCHAR(255) COMMENT '「code」- 子模块编码',
    `NAME`           VARCHAR(255) COMMENT '「name」- 子模块名称',
    `VERSION`        VARCHAR(64) COMMENT '「version」- 子模块版本', -- 模块版本
    `AUTHORIZED`     BIT         DEFAULT NULL COMMENT '「authorized」- 是否授权',

    `UI_ICON`        VARCHAR(255) COMMENT '「uiIcon」- 子模块图标',
    `UI_STYLE`       TEXT COMMENT '「uiStyle」- 子模块风格',
    `UI_SORT`        BIGINT COMMENT '「uiSort」- 子模块排序',

    -- 模块配置
    `UI_CONFIG`      LONGTEXT COMMENT '「uiConfig」- 子模块核心配置',
    `UI_CONTENT`     LONGTEXT COMMENT '「uiContent」- 配置数据',

    /*
     * 许可证相关信息
     */
    `SIGN_ISSUER`    VARCHAR(255) COMMENT '「signIssuer」- 许可证发证机构',
    `SIGN_NAME`      VARCHAR(255) COMMENT '「signName」- 许可证名称',
    `SIGN_AT`        DATETIME COMMENT '「signAt」- 发证时间',

    `SIGN_SECRET`    VARCHAR(64) COMMENT '「signSecret」- 证书专用密钥',
    `SIGN_LIC`       LONGTEXT COMMENT '「signLic」- 许可证内容',

    `SIGN_END`       DATETIME COMMENT '「signEnd」- 证书过期时间',
    `SIGN_START`     DATETIME COMMENT '「signStart」- 证书过期时间',
    /*
     * 整个Block所需的基本信息
     */
    `LIC_IDENTIFIER` LONGTEXT COMMENT '「licIdentifier」- 允许的模型标识',
    `LIC_MENU`       LONGTEXT COMMENT '「licMenu」- 该Block包含的菜单',

    -- 关联信息
    `APP_ID`         VARCHAR(36) COMMENT '「appId」- 关联的应用程序ID',
    `BAG_ID`         VARCHAR(36) COMMENT '「bagId」- 所属包ID',

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
-- changeset Lang:b-block-2
ALTER TABLE B_BLOCK
    ADD UNIQUE (`NAME`, `APP_ID`, `BAG_ID`);
ALTER TABLE B_BLOCK
    ADD UNIQUE (`CODE`, `APP_ID`);
ALTER TABLE B_BLOCK
    ADD UNIQUE (`SIGN_SECRET`);