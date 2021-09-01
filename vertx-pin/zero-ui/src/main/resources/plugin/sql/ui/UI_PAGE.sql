-- liquibase formatted sql

-- changeset Lang:ox-page-1
-- 应用程序中的页面表：UI_PAGE
DROP TABLE IF EXISTS UI_PAGE;
CREATE TABLE IF NOT EXISTS UI_PAGE
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 页面唯一主键',
    /*
     * /APP/MODULE/PAGE?PARAMS_STRING,
     * 三个参数构造唯一标识，路径统一，使用这三个条件查询页面信息
     * 1）page：查询的基本信息
     * 2）layout：查询对应的模板信息（无模板时则不使用模板）
     */
    `APP`              VARCHAR(32) COMMENT '「app」- 入口APP名称，APP中的path',
    `MODULE`           VARCHAR(32) COMMENT '「module」- 模块相关信息',
    `PAGE`             VARCHAR(32) COMMENT '「page」- 页面路径信息',

    -- 关联的 LAYOUT_ID （顶层模板信息，用于渲染模板专用）
    `LAYOUT_ID`        VARCHAR(36) COMMENT '「layoutId」- 使用的模板ID，最终生成 layout 顶层节点数据',
    -- 是否执行登录控制，安全页面需要执行登录控制炎症
    `SECURE`           BIT COMMENT '「secure」- 是否执行安全检查（安全检查才会被权限系统捕捉）',
    -- 当前页面是否包含了 Json 格式的参数信息：key = value，如果包含则存储对应的格式 ? 之后的内容，不考虑路径参数
    `PARAM_MAP`        TEXT COMMENT '「paramMap」- URL地址中的配置key=value',

    /*
     * 界面主要配置，深入到页面级别的基本配置
     * 针对页面进行处理
     * 1）容器名称：import Ox from 'oi'
     * 2) grid 布局用于解析当前页面的 grid 布局信息，并且和 control 连接
     */
    `CONTAINER_NAME`   VARCHAR(32) COMMENT '「containerName」- 当前页面是否存在容器，如果有容器，那么设置容器名称',
    `CONTAINER_CONFIG` TEXT COMMENT '「containerConfig」- 当前页面容器相关配置',
    `STATE`            TEXT COMMENT '「state」- 当前页面的初始化状态信息',
    `GRID`             TEXT COMMENT '「grid」- 当前页面的布局信息，Grid布局格式',
    `ASSIST`           TEXT COMMENT '「assist」- 当前页面的辅助数据Ajax配置',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-page-2
ALTER TABLE UI_PAGE
    ADD UNIQUE (`APP`, `MODULE`, `PAGE`, `SIGMA`) USING BTREE; -- 页面唯一地址，同一个应用内唯一

ALTER TABLE UI_PAGE
    ADD INDEX IDXM_UI_PAGE_APP_MODULE_PAGE_LANGUAGE_SIGMA (`APP`, `MODULE`, `PAGE`, `LANGUAGE`, `SIGMA`) USING BTREE;