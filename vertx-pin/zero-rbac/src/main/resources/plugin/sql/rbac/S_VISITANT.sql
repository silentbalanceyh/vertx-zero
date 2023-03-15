-- liquibase formatted sql

-- changeset Lang:ox-visitant-1
-- 视图访问者表，用于抽象视图扩展时使用
DROP TABLE IF EXISTS S_VISITANT;
CREATE TABLE IF NOT EXISTS S_VISITANT
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 限定记录ID',
    /*
     * 视图ID，当前资源访问者绑定的视图ID，当视图本身出现了 visitant = true 时，则执行
     * 资源访问者操作提取视图相关信息，和管理部分的 Seeker 相对应，管理部分当资源是 virtual = true 时
     * 则启用 Seeker 模式查找真实资源，而 Visitant 就是对真实资源的视图查询同步执行操作
     * View -> 绑定Resource -> ( virtual = true ) -> 查找对应资源访问者，执行资源访问
     * 1) 执行逻辑：
     * View -> seekSyntax / seekConfig / seekComponent
     * 模式支持：Replace / Extend
     * 2）作用阶段：
     * 作用周期包含三个：
     * 2.1) EAGER：该 ACL 直接控制当前请求资源（立即生效）
     * 2.2) DELAY：该 ACL 会读取成为配置项，作用于其他资源（在当前请求中不执行 ACL 流程）
     * -- 一般读取配置数据和元数据时使用 DELAY
     * -- 而读取当前数据时则直接使用 EAGER
     * 处于 DELAY 状态时，VIEW 中的 DataRegion 依旧生效
     */
    `VIEW_ID`     VARCHAR(36) COMMENT '「viewId」- 视图访问者的读ID',
    `MODE`        VARCHAR(36) COMMENT '「mode」- 模式，资源访问者继承于资源，可`替换/扩展`两种模式',
    `PHASE`       VARCHAR(64) COMMENT '「phase」- 作用周期',

    /*
     * 访问者类型决定了当前访问者如何处理视图相关信息
     * FORM: 表单访问者（字段属性访问）
     * LIST: 列表访问者（深度列过滤）
     * OP：操作访问者（操作处理）
     * VIEW: 视图访问者
     * 1）关于 seekKey
     * -- 在配置模式下，seekKey 描述的配置的 control 记录
     * -- 在记录读取下，seekKey 描述的是模型的 category 的主键
     **/
    `TYPE`        VARCHAR(128) COMMENT '「type」- 访问者类型',
    `IDENTIFIER`  VARCHAR(255) COMMENT '「identifier」- 动态类型中的模型ID',
    /*
     * 关于 seekKey
     * 1) 动态建模中，通常资源访问会牵涉到 controlId
     *    {
     *        "type": "LIST / FORM / OP",
     *        "controlId": "由于UI_CONTROL中定义了模型标识符，所以此处模型标识符可省略"
     *    }
     * 2) 静态建模中，通常资源访问主要会牵涉其他内容
     *    {
     *        "type": "LIST / FORM / OP",
     *        "workflow": "工作流"
     *    }
     * 关于Seek的整体结构
     * 管理端              消费端             定义端
     * S_PACKET           S_VISITANT        S_RESOURCE
     * SEEK_CONFIG                          SEEK_CONFIG
     * SEEK_SYNTAX        SEEK_KEY          SEEK_SYNTAX
     *                                      SEEK_COMPONENT
     * 抽象资源的提取，S_VIEW --> S_RESOURCE ( virtual = true ) --> S_VISITANT --> 计算最终的过滤部分内容 DM_ / ACL_
     * 抽象资源的定义，S_PATH --> webBind ( resource = packet ) --> S_PACKET -->
     * --- S_PATH绑定的资源操作（视图）
     * --- S_PATH绑定的资源操作（视图 + 资源访问者）
     * --- 上述分支出现在 resource 中的 virtual = true
     */
    `SEEK_KEY`    VARCHAR(255) COMMENT '「seekKey」- 资源检索的唯一键',

    -- 访问者的访问信息（列表）
    `DM_ROW`      TEXT COMMENT '「dmRow」对应视图中 Rows',
    `DM_QR`       TEXT COMMENT '「dmQr」对应视图中的 Criteria',
    `DM_COLUMN`   TEXT COMMENT '「dmColumn」对应视图中的 Projection',

    -- 访问者的访问信息（表单）
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
    ADD UNIQUE (`VIEW_ID`, `TYPE`, `SEEK_KEY`) USING BTREE;
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
        IDXM_S_VISITANT_VIEW_ID_TYPE_CONFIG (`VIEW_ID`, `TYPE`, `SEEK_KEY`);
ALTER TABLE S_VISITANT
    ADD INDEX
        IDXM_S_VISITANT_VIEW_ID_TYPE_IDENTIFIER (`VIEW_ID`, `TYPE`, `IDENTIFIER`);
