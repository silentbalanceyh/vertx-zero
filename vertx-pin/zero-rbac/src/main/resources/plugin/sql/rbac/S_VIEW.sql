-- liquibase formatted sql

-- changeset Lang:ox-view-1
-- 用户/ 角色资源限定表，用于处理资源的行、列基本信息
DROP TABLE IF EXISTS S_VIEW;
CREATE TABLE IF NOT EXISTS S_VIEW
(
    -- 资源拥有者：按角色/用户 处理
    `KEY`         VARCHAR(36) COMMENT '「key」- 限定记录ID',
    -- 用户优先模式，角色为默认（S_RESOURCE需要传入角色计算模式，多个角色处理时支持多角色的筛选字段需要保存在内
    `NAME`        VARCHAR(255) COMMENT '「name」- 视图名称，每个 MATRIX 对应一个视图',
    `TITLE`       VARCHAR(255) COMMENT '「title」- 视图标题，用户输入，可选择',
    `OWNER`       VARCHAR(36) COMMENT '「owner」- 用户 / 角色ID',
    `OWNER_TYPE`  VARCHAR(5) COMMENT '「ownerType」- ROLE 角色，USER 用户',
    `RESOURCE_ID` VARCHAR(36) COMMENT '「resourceId」- 关联资源ID',

    /*
     * 1. 针对该资源，只能查看对应的列
     *    CARD：卡片类型，针对数据库中的属性进行过滤
     *    LIST：列表类型，针对数据库中的列进行过滤
     *    FORM：表单类型，针对数据库中的字段进行过滤
     * 固定格式：
     * [field1, field2, field3]
     * -- 「后置列过滤」
     */
    `PROJECTION`  TEXT COMMENT '「projection」- 该资源的列定义（单用户处理）',
    /*
     * 2. 针对该资源，提供查询引擎的分支条件，和传入条件进行合并
     * 固定格式：
     * {} （查询引擎语法）
     * 当角色出现多个的时候，执行
     * {} or {} 的模式来执行查询
     * -- 「前置查询条件修改，只针对查询参数专用」
     */
    `CRITERIA`    TEXT COMMENT '「criteria」- 该资源的行查询（单用户处理）',
    /*
     * 3. 针对特殊资源，提供IDS的主键直接命中条件，用户处理复杂场景的直接Join（本来可以依靠查询引擎处理，但这里不设置）
     *    表单不存在这种类型，主要针对列表
     *    DATA：数据层面，在列表处理过程中，ROWS中存在的ID才会显示出来，不存在的不显示
     *    META：元数据层面，在处理过程中同样只读取ROWS中存在的ID信息
     *    （注）：对于界面呈现的ReadOnly模式，存在于UI的属性中，而不是出现在安全定义部分
     * 固定格式：（合并过滤）
     * {
     *     "field1": ["value1", "value2"],
     *     "field2": ["value2"]
     * }
     * -- 「后置行过滤」
     */
    `ROWS`        TEXT COMMENT '「rows」- 该资源针对保存的行进行过滤',
    `POSITION`    TEXT COMMENT '「position」- 当前列的顺序信息',

    /*
     * 是否虚拟视图，一般在抽象层的为虚拟视图，一旦出现虚拟视图，则需要针对原始视图执行视图替换
     * 1）入参为传入的参数信息，主要用于检查 identifier 部分
     * 2）只能在顶层模型往底层模型中转移的时候执行虚拟视图的控制
     * 3）虚拟视图提供的功能
     * 3.1）列过滤，从配置项到子类
     * 3.2）表单三态：可见/不可见，可见时：只读/可编辑
     * 3.3）操作：不同的操作带有不同的扩展
     * 如果当前视图带有视图访问者，则计算算法会发生改变，主要作用于通用接口，目前需要设置的主要是：
     * 一旦启用了访问者，那么该视图中对应的 ROWS/CRITERIA/PROJECTION/POSITION 会纳入到系统中
     * 运算，运算时需要考虑访问者中的扩展属性
     */
    `VISITANT`    BIT COMMENT '「visitant」- 是否包含了视图访问者',

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
    PRIMARY KEY
        (
         `KEY`
            ) USING BTREE
);

-- changeset Lang:ox-view-2
-- Unique
-- 用户、资源：唯一记录：高优先级
-- 角色、资源：唯一记录：低优先级
ALTER TABLE S_VIEW
    ADD UNIQUE (`OWNER`, `OWNER_TYPE`, `RESOURCE_ID`, `NAME`) USING BTREE;