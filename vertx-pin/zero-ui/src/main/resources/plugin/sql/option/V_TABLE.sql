-- liquibase formatted sql

-- changeset Lang:ox-option-table-1
-- 表格选项：V_TABLE
DROP TABLE IF EXISTS V_TABLE;
CREATE TABLE IF NOT EXISTS V_TABLE
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 表选项主键',
    -- 基本选项
    `BORDERED`         BIT COMMENT '「bordered」- 是否带表框',
    `SIZE`             VARCHAR(16) COMMENT '「size」- 表格尺寸',
    `CLASS_NAME`       VARCHAR(128) COMMENT '「className」- CSS属性',
    -- 特殊信息
    `TOTAL_REPORT`     VARCHAR(128) COMMENT '「totalReport」- total.report - 文字: 总共多少条统计',
    `TOTAL_SELECTED`   VARCHAR(128) COMMENT '「totalSelected」- total.selected - 文字: 选择了多少条',
    -- 行选择，主要用于 onRow
    `ROW_DOUBLE_CLICK` VARCHAR(64) COMMENT '「rowDoubleClick」- row.onDoubleClick - 双击事件名',
    `ROW_CLICK`        VARCHAR(64) COMMENT '「rowClick」- row.onClick - 单击事件名',
    `ROW_CONTEXT_MENU` VARCHAR(64) COMMENT '「rowContextMenu」- row.onContextMenu - 右键菜单事件名',
    `ROW_MOUSE_ENTER`  VARCHAR(64) COMMENT '「rowMouseEnter」- row.onMouseEnter - 鼠标左键事件名',
    `ROW_MOUSE_LEAVE`  VARCHAR(64) COMMENT '「rowMouseLeave」- row.onMouseLeave - 鼠标移开事件名',
    -- Executor 专用 columns（第一列/或最后一列）
    `OP_TITLE`         VARCHAR(255) COMMENT '「opTitle」- columns/[0]/title, 执行列标题',
    `OP_DATA_INDEX`    VARCHAR(255) COMMENT '「opDataIndex」- columns/[0]/dataIndex, 执行列标题',
    `OP_FIXED`         BIT DEFAULT FALSE COMMENT '「opFixed」- columns/[0]/fixed，执行列左还是右',
    `OP_CONFIG`        TEXT COMMENT '「opConfig」- columns/[0]/$option, 执行类对应配置，配置按钮',
    PRIMARY KEY (`KEY`) USING BTREE
);