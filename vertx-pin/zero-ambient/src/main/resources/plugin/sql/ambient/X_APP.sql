-- liquibase formatted sql

-- changeset Lang:ox-app-1
-- 应用程序表：X_APP
DROP TABLE IF EXISTS X_APP;
CREATE TABLE IF NOT EXISTS X_APP
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 应用程序主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 应用程序名称',
    `CODE`       VARCHAR(36) COMMENT '「code」- 应用程序编码',

    -- 常用属性
    `TITLE`      VARCHAR(64) COMMENT '「title」- 应用程序标题',
    `LOGO`       LONGTEXT COMMENT '「logo」- 应用程序图标',
    `ICP`        VARCHAR(64) COMMENT '「icp」- ICP备案号',
    `COPY_RIGHT` VARCHAR(255) COMMENT '「copyRight」- CopyRight版权信息',
    `EMAIL`      VARCHAR(255) COMMENT '「email」- 应用Email信息',

    -- 部署常用
    `DOMAIN`     VARCHAR(255) COMMENT '「domain」- 应用程序所在域',
    `APP_PORT`   INTEGER COMMENT '「appPort」- 应用程序端口号，和SOURCE的端口号区别开',
    `URL_ENTRY`  VARCHAR(255) COMMENT '「urlEntry」— 应用程序入口页面（登录页）',
    `URL_MAIN`   VARCHAR(255) COMMENT '「urlMain」- 应用程序内置主页（带安全）',

    -- 两个路由和标识
    `PATH`       VARCHAR(255) COMMENT '「path」- 应用程序路径',
    `ROUTE`      VARCHAR(255) COMMENT '「route」- 后端API的根路径，启动时需要',
    `APP_KEY`    VARCHAR(128) COMMENT '「appKey」- 应用程序专用唯一hashKey',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-app-2
-- Unique JsonKeys：独立唯一键定义
ALTER TABLE X_APP
    ADD UNIQUE (`CODE`) USING BTREE;
ALTER TABLE X_APP
    ADD UNIQUE (`PATH`, `URL_ENTRY`) USING BTREE; -- 应用唯一入口
ALTER TABLE X_APP
    ADD UNIQUE (`PATH`, `URL_MAIN`) USING BTREE; -- 应用唯一主页
ALTER TABLE X_APP
    ADD UNIQUE (`NAME`) USING BTREE;
-- 应用程序名称唯一（这是系统名称）

-- /app/name/:name
ALTER TABLE X_APP
    ADD INDEX IDX_X_APP_NAME (`NAME`) USING BTREE;