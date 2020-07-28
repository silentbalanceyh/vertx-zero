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

    /*
     * 核心流程
     * 定义部分：
     * 1）直接按照 code 读取 S_PATH
     * 2）根据是否分组设置记录基础信息
     * 2.1) 如果不分组，uiCondition 是静态
     * 2.2) 如果分组，先计算 group 部分的内容
     * ----- 选中的 group 和 groupMapping 计算出 uiCondition
     * 3）触发 uiCondition，然后底层根据配置数据计算数据源
     * 4）根据最终拿到的数据源 和 uiConfig 计算最终界面
     * 消费部分：
     * 1）直接根据角色和 S_PACKET 中的维度提取 resource 的资源 ID 集合
     * 2）读取这种角色的 S_VIEW 中的核心配置信息
     */
    `GROUP_MAPPING`       TEXT COMMENT '「mapping」- 从 group -> ui 转换',

    /*
     * 分组类型主要牵涉三种
     * NONE : 不分组
     * DICT : 字典分组，对应 Tabular 部分的分类（直接列表）
     * TREE : 树形分类，对应 Category 部分的分类
     */
    `GROUP_TYPE`        VARCHAR(128) COMMENT '「groupType」- 分组类型',
    `GROUP_COMPONENT`   VARCHAR(255) COMMENT '「groupComponent」- 必须绑定组专用Dao组件',
    `GROUP_CONDITION`   TEXT COMMENT '「groupCondition」- 分组条件',
    `GROUP_CONFIG`      TEXT COMMENT '「groupConfig」- 组配置信息，配置呈现部分',

    /*
     * 描述了目标界面的条件信息
     * WEB：静态的，类似 items，直接在 config 节点中定义
     * DAO：动态的，使用静态的 DAO 去读取数据源信息
     * OX：动态的Dao，使用类似 /api/ox/ 下的底层逻辑实现
     * DEF: 自定义，可使用组件模式
     **/
    `UI_TYPE`       TEXT COMMENT '「uiType」- 目标数据源类型',
    `UI_CONFIG`     TEXT COMMENT '「uiConfig」- 界面配置',
    `UI_CONDITION`  TEXT COMMENT '「uiCondition」- 查询模板',
    `UI_COMPONENT`  VARCHAR(255) COMMENT '「uiComponent」- 在 DAO/OX/DEF 时的特殊组件',

    `SIGMA`      VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`   VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`     BIT COMMENT '「active」- 是否启用',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:ox-path-2
-- Unique Key：独立唯一主键
ALTER TABLE S_PATH
    ADD UNIQUE (`CODE`, `SIGMA`);