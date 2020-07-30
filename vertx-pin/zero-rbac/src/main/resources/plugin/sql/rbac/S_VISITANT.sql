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
     **/
    `TYPE`                 VARCHAR(128) COMMENT '「type」- 访问者类型',
    `STATIC_KEY`           VARCHAR(36)  COMMENT '「staticKey」- 模型下记录对应的ID，一般是配置的ID',
    `DYNAMIC_ID`           VARCHAR(255) COMMENT '「dynamicId」- 动态类型中的模型ID',

    /*
     * 此处的 MODEL_KEY 用于表示某个表单，列表的记录集
     * RELATED_ID 对应 controlId 记录，在处理单个 controlId 记录时启用访问者
     **/
    `RELATED_ID`        VARCHAR(255) COMMENT '「relatedId」- 相关模型',
    `RELATED_RESOURCE`  TEXT COMMENT '「relatedResource」- 相关资源影响集合（主要用于写限制）',
    `RELATED_CONFIG`    TEXT COMMENT '「relatedConfig」- 相关资源影响配置（主要用于写限制）',

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
    `ACL_VISIBLE`          TEXT COMMENT '「aclVisible」- 可见的属性集',
    `ACL_VIEW`             TEXT COMMENT '「aclView」- 只读的属性集',
    `ACL_VARIETY`          TEXT COMMENT '「aclVariety」- 多样性的属性集，用于控制集合类型的属性',
    `ACL_VARIETY_CONFIG`   TEXT COMMENT '「aclVarietyConfig」- 多样性的属性集相关配置',
    `ACL_VOW`              TEXT COMMENT '「aclVow」- 引用类属性集',
    `ACL_VOW_CONFIG`       TEXT COMMENT '「aclVowConfig」- 引用类属性集相关配置',
    `ACL_DEPEND`           TEXT COMMENT '「aclDepend」- 依赖属性集',
    `ACL_DEPEND_CONFIG`    TEXT COMMENT '「aclDependConfig」- 依赖属性集配置',

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
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-visitant-2
ALTER TABLE S_VISITANT
    ADD UNIQUE (`VIEW_ID`,`TYPE`,`STATIC_KEY`);
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
ALTER TABLE S_VISITANT ADD INDEX
    IDXM_S_VISITANT_VIEW_ID_TYPE_RELATED (`VIEW_ID`,`TYPE`,`RELATED_ID`);
