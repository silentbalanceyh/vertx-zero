-- liquibase formatted sql

-- changeset Lang:ui-view-1
-- 控件表：UI_VIEW
DROP TABLE IF EXISTS UI_VIEW;
CREATE TABLE IF NOT EXISTS UI_VIEW
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 查询记录ID',
    /*
     * 此处需要说明的是 UI_VIEW 只挂载在 LIST 中，所以有几个维度需要说明
     * -- CODE值为当前系统内码，传参专用
     * -- NAME/POSITION对应的就是视图 SView 中的 NAME/POSITION
     */
    `NAME`         VARCHAR(255) COMMENT '「name」- 视图名称，每个 MATRIX 对应一个视图',
    `CODE`         VARCHAR(255) COMMENT '「code」- 系统编码',
    `SORT`         INT COMMENT '「sort」- QR的顺序',

    /*
     * 追加维度
     * -- 按模型
     * -- 按流程
     * 一个模型可能包含多个流程，此处做开放的新维度
     */
    `IDENTIFIER`   VARCHAR(255) COMMENT '「identifier」- 模型标识符',
    `WORKFLOW`     VARCHAR(255) COMMENT '「workflow」- 工作流名称',

    `VIEW`         VARCHAR(96) COMMENT '「view」- 视图名',
    `POSITION`     VARCHAR(96) COMMENT '「position」- 当前视图的模块位置，比页面低一个维度',

    `TITLE`        VARCHAR(255) COMMENT '「title」- 视图标题，用户输入，可选择',
    `PROJECTION`   TEXT COMMENT '「projection」- 该资源的列定义',
    `CRITERIA`     TEXT COMMENT '「criteria」- 该资源的行查询',
    `ROWS`         TEXT COMMENT '「rows」- 该资源针对保存的行进行过滤',
    `UI_CONFIG`    LONGTEXT COMMENT '「uiConfig」- 界面配置',

    /*
     * 多出一个维度是执行QR查询条件的特殊计算，计算结果要保证可支持
     * 排序，分页，列过滤几种
     */
    `QR_COMPONENT` VARCHAR(255) COMMENT '「qrComponent」- 查询条件专用组件',
    `QR_CONFIG`    TEXT COMMENT '「qrConfig」- 查询组件专用配置',

    -- 特殊字段
    `SIGMA`        VARCHAR(128) COMMENT '「sigma」- 用户组绑定的统一标识',
    `LANGUAGE`     VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`       BIT COMMENT '「active」- 是否启用',
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ui-view-2
-- 业务集
/*
 * sigma    - 租户级
 * name     - 查询名
 * code     - 标识当前列表的 code  ( 标识所属列表 )
 * view     - 当前视图名          ( 标识所属安全视图 )
 * position - 当前视图位置        ( 标识所属安全视图 )
 */
ALTER TABLE UI_VIEW
    ADD UNIQUE (`SIGMA`, `CODE`, `NAME`);
ALTER TABLE UI_VIEW
    ADD UNIQUE (`SIGMA`, `CODE`, `VIEW`, `POSITION`);