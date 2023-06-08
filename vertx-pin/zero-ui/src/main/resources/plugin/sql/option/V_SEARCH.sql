-- liquibase formatted sql

-- changeset Lang:ox-option-search-1
-- 搜索选项：V_SEARCH
DROP TABLE IF EXISTS V_SEARCH;
CREATE TABLE IF NOT EXISTS V_SEARCH
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 选项主键',
    `ENABLED`         BIT COMMENT '「enabled」- search.enabled: 是否启用搜索',
    `ADVANCED`        BIT COMMENT '「advanced」- search.advanced: 是否启用高级搜索',
    `OP_REDO`         VARCHAR(64) COMMENT '「opRedo」- search.op.redo: 清除条件按钮提示文字',
    `OP_ADVANCED`     VARCHAR(64) COMMENT '「opAdvanced」- search.op.advanced: 高级搜索按钮提示文字',
    `OP_VIEW`         VARCHAR(64) COMMENT '「opView」- search.op.view：视图查询条件修改文字',
    `CONFIRM_CLEAR`   VARCHAR(255) COMMENT '「confirmClear」- search.confirm.clear: 清除条件提示',
    `PLACEHOLDER`     VARCHAR(255) COMMENT '「placeholder」- search.placeholder: 搜索框水印文字',
    `COND`            TEXT COMMENT '「cond」- search.cond: 搜索条件',
    `ADVANCED_WIDTH`  VARCHAR(100) COMMENT '「advancedWidth」- search.advanced.width: 高级搜索窗口宽度',
    `ADVANCED_TITLE`  VARCHAR(128) COMMENT '「advancedTitle」- search.advanced.title: 高级搜索窗口标题',
    `ADVANCED_NOTICE` TEXT COMMENT '「advancedNotice」- search.advanced.notice: 提示信息结构（Alert）',
    `ADVANCED_VIEW`   TEXT COMMENT '「viewOption」- search.criteria.xxx：视图选项信息',
    PRIMARY KEY (`KEY`) USING BTREE
);