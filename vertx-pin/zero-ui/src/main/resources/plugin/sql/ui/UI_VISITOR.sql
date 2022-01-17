-- liquibase formatted sql

-- changeset Lang:ui-visitor-1
-- 向量表：UI_VISITOR
DROP TABLE IF EXISTS UI_VISITOR;
CREATE TABLE IF NOT EXISTS UI_VISITOR
(
    /*
     * 四个维护的核心设计
     * - page:           页面ID定位
     * - identifier：     模型标识定位
     * - path:            路径信息
     *
     * 1. 资产管理界面
     * - 1.1. 先进入页面（页面ID唯一）
     * - 1.2. 左侧菜单选择执行（动态可变，限定identifier）
     * - 1.3. 路径信息：配置部分 + 视图View + 位置Position
     *
     * 2. 权限管理界面
     * - 2.1. 左侧菜单选择执行（动态可变）
     * - 2.2. 选择该操作关联资源，和资源访问者协同更改
     *
     * 3. 唯一标识：identifier + page + path 可计算得到唯一的 control_id 值
     */
    `IDENTIFIER`    VARCHAR(36) COMMENT '「identifier」- 维度1：标识模型',
    `PAGE`          VARCHAR(36) COMMENT '「page」- 维度2：页面ID',
    `PATH`          VARCHAR(128) COMMENT '「path」- 维度3：路径信息，view + position',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 维度4：操作类型：List / Form 或其他',


    /*
     * 核心的两个ID
     * - controlId 负责消费和使用
     * - resourceId 负责管理
     */
    `CONTROL_ID`    VARCHAR(36) COMMENT '「controlId」- 挂载专用的ID：List / Form 都可用',
    `RESOURCE_ID`   VARCHAR(36) COMMENT '「resourceId」- 关联资源ID',

    `SIGMA`         VARCHAR(128) COMMENT '「sigma」- 高维度：统一标识符',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',
    `RUN_COMPONENT` TEXT COMMENT '「runComponent」- 执行组件，扩展时专用',
    PRIMARY KEY (`IDENTIFIER`, `PAGE`, `PATH`, `TYPE`, `SIGMA`)
);