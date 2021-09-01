-- liquibase formatted sql

-- changeset Lang:ox-visitant-1
-- 视图访问者表，用于抽象视图扩展时使用
DROP TABLE IF EXISTS S_VISITANT;
CREATE TABLE IF NOT EXISTS S_VISITANT
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 限定记录ID',

    -- 视图ID
    `VIEW_ID`     VARCHAR(36) COMMENT '「viewId」- 视图访问者的读ID',
    /*
     * 作用周期包含三个：
     * 1）EAGER：该 ACL 直接控制当前请求资源（立即生效）
     * 2）DELAY：该 ACL 会读取成为配置项，作用于其他资源（在当前请求中不执行 ACL 流程）
     * -- 一般读取配置数据和元数据时使用 DELAY
     * -- 而读取当前数据时则直接使用 EAGER
     * 处于 DELAY 状态时，VIEW 中的 DataRegion 依旧生效
     */
    `PHASE`       VARCHAR(64) COMMENT '「phase」- 作用周期',
    /*
     * 查询条件
     * 1）VIEW_ID：角色/资源定位视图
     * 2）TYPE：FORM/LIST/OP 三大类（抽象资源分类）
     * 3）RELATED_KEY：定位唯一记录专用，FORM 和 LIST 为 controlId
     */

    /*
     * 访问者类型决定了当前访问者如何处理视图相关信息
     * FORM: 表单访问者（字段属性访问）
     * LIST：列表访问者（深度列过滤）
     * OP：操作访问者（操作处理）
     * 1）关于 configKey
     * -- 在配置模式下，configKey 描述的配置的 control 记录
     * -- 在记录读取下，configKey 描述的是模型的 category 的主键
     **/
    `TYPE`        VARCHAR(128) COMMENT '「type」- 访问者类型',
    `IDENTIFIER`  VARCHAR(255) COMMENT '「identifier」- 动态类型中的模型ID',
    `CONFIG_KEY`  VARCHAR(36) COMMENT '「configKey」- 模型下记录对应的ID，一般是配置的ID',

    -- 访问者的访问信息
    /*
     * 1）可见性：属性是否可访问，不可访问的数据直接在数据层滤掉
     * 2）只读：属性是否只读
     * 3）可编辑：可编辑 = 可见性 - 只读
     * 4）多样性属性：所有数组类的多样性属性集
     * 5）多样性配置：递归三种属性集，标记每种属性集的配置信息
     * 6）依赖属性集：保存了所有带有依赖属性的信息
     * 7）依赖属性集配置
     */
    `ACL_VISIBLE` TEXT COMMENT '「aclVisible」- 可见的属性集',
    `ACL_VIEW`    TEXT COMMENT '「aclView」- 只读的属性集',
    `ACL_VARIETY` TEXT COMMENT '「aclVariety」- 多样性的属性集，用于控制集合类型的属性',
    `ACL_VOW`     TEXT COMMENT '「aclVow」- 引用类属性集',
    `ACL_VERGE`   TEXT COMMENT '「aclVerge」- 依赖属性集',

    -- 特殊字段
    `SIGMA`       VARCHAR(128) COMMENT '「sigma」- 用户组绑定的统一标识',
    `LANGUAGE`    VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`      BIT COMMENT '「active」- 是否启用',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-visitant-2
ALTER TABLE S_VISITANT
    ADD UNIQUE (`VIEW_ID`, `TYPE`, `CONFIG_KEY`) USING BTREE;
/*
 * 关于唯一性描述
 * 1）读取配置，如表单读取
 * 表单读取：
 * -- VIEW_ID：读取表单的资源窗口
 * -- TYPE：手工定义的类型（静态固定）
 * -- STATIC_KEY：关联的这种类型的主键
 ***：这种情况下，如果 STATIC_TYPE 固定了，那么访问表就固定了，直接通过 STATIC_KEY 执行抽象转具体的过程
 * 而且最终控制会下放到 Field/Column 中
 * 操作读取：
 * -- VIEW_ID：读取操作的资源接口
 * -- TYPE：本身固定
 * -- STATIC_KEY：操作所属 LIST/FORM 两种实体，此处 STATIC_KEY 依旧是 control
 * 2）读取数据本身，如配置项读取
 * -- VIEW_ID：读取操作的资源接口
 * -- DYNAMIC_ID：动态类型，映射到 identifier 的模型 ID
 */
ALTER TABLE S_VISITANT
    ADD INDEX
        IDXM_S_VISITANT_VIEW_ID_TYPE_CONFIG (`VIEW_ID`, `TYPE`, `CONFIG_KEY`) USING BTREE;
ALTER TABLE S_VISITANT
    ADD INDEX
        IDXM_S_VISITANT_VIEW_ID_TYPE_IDENTIFIER (`VIEW_ID`, `TYPE`, `IDENTIFIER`) USING BTREE;
