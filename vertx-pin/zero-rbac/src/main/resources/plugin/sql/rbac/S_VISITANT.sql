-- liquibase formatted sql

-- changeset Lang:ox-visitant-1
-- 视图访问者表，用于抽象视图扩展时使用
DROP TABLE IF EXISTS S_VISITANT;
CREATE TABLE IF NOT EXISTS S_VISITANT
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 限定记录ID',
    `CODE`        VARCHAR(255) COMMENT '「code」- 访问者系统编码',
    `VIEW_ID`     VARCHAR(36) COMMENT '「viewId」- 视图访问者的ID',

    /*
     * 访问者类型决定了当前访问者如何处理视图相关信息
     * FORM: 表单访问者（字段属性访问）
     * LIST：列表访问者（深度列过滤）
     * OP：操作访问者（操作处理）
     **/
    `TYPE` VARCHAR(128) COMMENT '「type」- 访问者类型',

    /*
     * 此处的 MODEL_KEY 用于表示某个表单，列表的记录集
     * MODEL_KEY 对应 controlId 记录，在处理单个 controlId 记录时启用访问者
     * 而 MODEL_ID 用于表示当前访问者所属的模型 identifier 统一标识符
     **/
    `MODEL_KEY`   VARCHAR(36)  COMMENT '「modelKey」- 模型下记录对应的ID',
    `MODEL_ID`    VARCHAR(255) COMMENT '「modelId」- 模型对应的 identifier ',

    -- 访问者的访问信息
    /*
     * 1）可见性：属性是否可访问，不可访问的数据直接在数据层滤掉
     * 2）只读：属性是否只读
     * 3）可编辑：可编辑 = 可见性 - 只读
     * 4）多样性属性：所有数组类的多样性属性集
     * 5）多样性配置：递归三种属性集，标记每种属性集的配置信息
     */
    `ATTR_VISIBLE`          TEXT COMMENT '「attrVisible」- 可见的属性集',
    `ATTR_VIEW`             TEXT COMMENT '「attrView」- 只读的属性集',
    `ATTR_VARIETY`          TEXT COMMENT '「attrVariety」- 多样性的属性集，用于控制集合类型的属性',
    `ATTR_VARIETY_CONFIG`   TEXT COMMENT '「attrVarietyConfig」- 多样性的属性集相关配置',
    `ATTR_VOW`              TEXT COMMENT '「attrVow」- 引用类属性集',
    `ATTR_VOW_CONFIG`       TEXT COMMENT '「attrVowConfig」- 引用类属性集相关配置',

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
    ADD UNIQUE (`CODE`, `VIEW_ID`);
ALTER TABLE S_VISITANT ADD INDEX
    IDXM_S_VISITANT_VIEW_ID_TYPE_MODEL (`VIEW_ID`,`TYPE`,`MODEL_ID`,`MODEL_KEY`);
