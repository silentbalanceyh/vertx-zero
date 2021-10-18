-- liquibase formatted sql

-- changeset Lang:ox-option-fragment-1
-- 组件选项：V_FRAGMENT
DROP TABLE IF EXISTS V_FRAGMENT;
CREATE TABLE IF NOT EXISTS V_FRAGMENT
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 选项主键',
    `CONTAINER`      TEXT COMMENT '「container」- 容器专用格式',
    `NOTICE`         TEXT COMMENT '「notice」- notice选项，Alert结构',
    `BUTTON_CONNECT` VARCHAR(36) COMMENT '「buttonConnect」- 按钮的ID（单按钮）',
    `BUTTON_GROUP`   TEXT COMMENT '「buttonGroup」- 一组按钮配置',
    `MODAL`          TEXT COMMENT '「modal」- modal选项，Model专用结构',
    `GRID`           INT DEFAULT 3 COMMENT '「grid」- grid选项（分区面板专用）',
    `CONFIG`         TEXT COMMENT '「config」- 根目录开始的基本配置',
    PRIMARY KEY (`KEY`) USING BTREE
);