-- liquibase formatted sql

-- changeset Lang:ox-path-1
-- 权限定义规则专用表，资源路径表：S_PATH
/**
 * 主控资源路径描述信息，描述信息中定义了搜索路径的方式
 * 资源路径搜索方式的基本定义，和界面有关
 * 类型，该表属于业务对接接口，它包含了一个完整的资源描述信息
 * 1）目前支持的四种核心描述类型：
 * -- 1.1）菜单管理，针对 /api/menus 的单独管理
 * -- 1.2）表单管理，先分类 -> 选择表单 -> 针对单个表单的特殊管理 /ui/control
 *    -- 表单内部 type = FORM 的配置读取过程，限定字段的读写
 * -- 1.3）列管理，主要针对 full-columns 部分的过滤
 * -- 1.4）操作管理，针对静态资源和动态资源的 op 过滤
 * 2）每个路径下包含多个 packet 实例，最终结构：
 * -- path1 -> packet1 -> resource1
 * --       -> packet2 -> resource2
 * 3）视图属性设置（管理视图）
 * 行操作的直接选择（菜单管理）
 * 分类数据源（先分类），再查找 packet 然后执行保存过滤
 *
 **/
DROP TABLE IF EXISTS S_PATH;
CREATE TABLE IF NOT EXISTS S_PATH
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 规则主键',

    `NAME`          VARCHAR(255) COMMENT '「name」- 规则名称',
    `CODE`          VARCHAR(255) COMMENT '「code」- 系统界面标识',
    `PHASE`         VARCHAR(64) COMMENT '「phase」- UI读取数据的操作周期',
    `MAPPING`       TEXT COMMENT '「mapping」- 从 dm -> ui 转换',

    /*
     * 1. 非区域模式：1 DM - 1 UI，父代码为空           核心组件：HSUiNorm   dmCode = NULL
     * 2. 区域模式下：1 DM - n UI，此时父代码不为空      核心组件：HSUiArea   dmCode = 父代码
     *    区域模式下组件内部实现第二次读取，如流程管理，区域会触发
     *    -- 视图管理
     *    -- 操作管理
     *    此时的管理会执行降维操作
     */
    `PARENT_ID`     VARCHAR(36) COMMENT '「parentId」- 区域模式下的父ID，系统内部读取',

    /*
     * 核心流程
     * 定义部分：
     * 1）直接按照 code 读取 S_PATH
     * 2）根据是否分组设置记录基础信息
     * 2.1) 如果不分组，uiCondition 是静态
     * 2.2) 如果分组，先计算 dm 部分的内容
     * ----- 选中的 dm 和 dmMapping 计算出 uiCondition
     * 3）触发 uiCondition，然后底层根据配置数据计算数据源
     * 4）根据最终拿到的数据源 和 uiConfig 计算最终界面
     * 消费部分：
     * 1）直接根据角色和 S_PACKET 中的维度提取 resource 的资源 ID 集合
     * 2）读取这种角色的 S_VIEW 中的核心配置信息
     */
    `RUN_COMPONENT` VARCHAR(255) COMMENT '「runComponent」- HValve执行组件，组件内置处理 dm / ui 两部分内容',
    `RUN_TYPE`      VARCHAR(5) COMMENT '「runType」- 视图管理类型（查询用）',

    /*
     * 分组类型主要牵涉四种
     * ScDim
     * - NONE:      无维度，平行定义
     * - FLAT:      列表型（只分区域）
     * - TREE:      树型
     * - FOREST:    森林型（列表 + 树）
     */
    `DM_TYPE`       VARCHAR(128) COMMENT '「dmType」- 分组类型',
    `DM_COMPONENT`  VARCHAR(255) COMMENT '「dmComponent」- 必须绑定组专用Dao组件',
    `DM_CONDITION`  TEXT COMMENT '「dmCondition」- 分组条件',
    `DM_CONFIG`     TEXT COMMENT '「dmConfig」- 组配置信息，配置呈现部分',

    /*
     * 描述了目标界面的条件信息
     * - NONE:        默认值，无数据源
     * - WEB:         静态的，类似 items，直接在 config 节点中定义
     * - DAO:         动态的，使用静态的 DAO 去读取数据源信息
     * - ATOM:        动态的Dao，使用类似 /api/ox/ 下的底层逻辑实现
     * - DEFINE:      自定义，可使用组件模式（此时才开启uiComponent的使用）
     **/
    `UI_TYPE`       VARCHAR(255) COMMENT '「uiType」- 目标数据源类型',
    `UI_CONFIG`     TEXT COMMENT '「uiConfig」- 界面配置',
    `UI_CONDITION`  TEXT COMMENT '「uiCondition」- 查询模板',
    `UI_COMPONENT`  VARCHAR(255) COMMENT '「uiComponent」- 特殊组件',
    `UI_SURFACE`    TEXT COMMENT '「uiSurface」- 界面呈现模式，已经被降维之后（列表、树、其他等相关配置）',
    `UI_SORT`       INT COMMENT '「sort」- 该板块的排序（前端）',

    `SIGMA`         VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:ox-path-2
-- Unique Key：独立唯一主键
ALTER TABLE S_PATH
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;
-- 场景：权限管理
ALTER TABLE S_PATH
    ADD INDEX IDX_S_PATH_RUN_TYPE_SIGMA (`RUN_TYPE`, `SIGMA`) USING BTREE;
