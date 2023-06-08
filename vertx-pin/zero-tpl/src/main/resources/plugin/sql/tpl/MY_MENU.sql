-- liquibase formatted sql

-- changeset Lang:my-menu-1
-- 个人菜单表：MY_MENU
DROP TABLE IF EXISTS MY_MENU;
CREATE TABLE IF NOT EXISTS MY_MENU
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 菜单主键',
    -- UI呈现
    `ICON`        VARCHAR(255) COMMENT '「icon」- 菜单使用的icon',
    `TEXT`        VARCHAR(255) COMMENT '「text」- 菜单显示文字',
    `URI`         VARCHAR(255) COMMENT '「uri」- 菜单地址（不包含应用的path）',


    -- 主菜单定制专用（呈现效果）
    `UI_SORT`     BIGINT COMMENT '「uiSort」- 菜单排序',
    `UI_PARENT`   VARCHAR(36) COMMENT '「uiParent」- 菜单父ID',
    `UI_COLOR_FG` VARCHAR(16) COMMENT '「uiColorFg」- 前景色',
    `UI_COLOR_BG` VARCHAR(16) COMMENT '「uiColorBg」- 背景色',


    -- 维度控制
    `TYPE`        VARCHAR(32) COMMENT '「type」- 菜单类型',
    `PAGE`        VARCHAR(64) COMMENT '「page」- 菜单所在页面',
    `POSITION`    VARCHAR(16) COMMENT '「position」- 菜单位置',

    `OWNER`       VARCHAR(36) COMMENT '「owner」- 拥有者ID，我的 / 角色级',
    `OWNER_TYPE`  VARCHAR(5) COMMENT '「ownerType」- ROLE 角色，USER 用户',

    -- 特殊字段
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',


    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:my-menu-2
/*
 * 五个维度，近似于视图
 * - owner：用户ID
 * - type：个人菜单类型
 *   - NAV：导航菜单（主页/工作台）
 *   - MENU：主菜单
 *   - CONTEXT：右键菜单
 * - page：页面路径
 * - position：位置（双导航模式）
 * - uiMenu：菜单关联ID
 *
 * 1）如果 type = MENU，则 PAGE = ALL, POSITION = APP
 * 2）其他情况，必须 page 和 position 参数
 */
ALTER TABLE MY_MENU
    ADD UNIQUE (`OWNER_TYPE`, `OWNER`, `TYPE`, `PAGE`, `POSITION`, `URI`);