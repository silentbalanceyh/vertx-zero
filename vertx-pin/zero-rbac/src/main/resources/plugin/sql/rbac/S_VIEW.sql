-- liquibase formatted sql

-- changeset Lang:ox-view-1
-- 用户/ 角色资源限定表，用于处理资源的行、列基本信息
DROP TABLE IF EXISTS S_VIEW;
CREATE TABLE IF NOT EXISTS S_VIEW
(
    -- 资源拥有者：按角色/用户 处理
    `KEY`         VARCHAR(36) COMMENT '「key」- 限定记录ID',
    -- 用户优先模式，角色为默认（S_RESOURCE需要传入角色计算模式，多个角色处理时支持多角色的筛选字段需要保存在内
    `NAME`        VARCHAR(96) COMMENT '「name」- 视图名称，每个 MATRIX 对应一个视图',
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
    `POSITION`    VARCHAR(96) COMMENT '「position」- 当前视图的模块位置，比页面低一个维度',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-view-2
-- Unique
-- 用户、资源：唯一记录：高优先级
-- 角色、资源：唯一记录：低优先级
ALTER TABLE S_VIEW
    ADD UNIQUE (
                `OWNER_TYPE`, -- 拥有者类型（角色视图和个人视图的分离）
                `OWNER`, -- 拥有者（角色ID和用户ID）对应拥有的单实体
                `RESOURCE_ID`, -- 资源ID，对应某一个查询接口
                `NAME`, -- 视图名称（用户创建视图以及角色创建视图时专用）
                `POSITION` -- 视图位置（最复杂的维度）
        ) USING BTREE;
/*
 POSITION 是视图中的核心维度，在系统中出现不同情况则对其进行管理
 1. 单模块无视图管理
    NAME = DEFAULT,
    POSITION = DEFAULT
 2. 单模块开启视图应用
    NAME = DEFAULT, V1, V2, V3
    POSITION = DEFAULT
 3. 多模块无视图管理
    NAME = DEFAULT
    POSITION = DEFAULT, P1, P2, P3, P4
 4. 多模块多视图管理
    NAME = DEFAULT, V1, V2, V3
    POSITION = DEFAULT, P1, P2, P3, P4

 关于位置的详细说明
 1. POSITION 比资源多一个维度，资源通常是类似 /api/xxx/search 的接口，但这个接口可能
    会跨越页面（两个菜单共享一个资源，但查询条件不同）。
 2. POSITION 不可以和页面绑定，比如待办列表，是一个 Tab 页签造成了同一个页面中出现了两种
    不同的查询，绑定了同一个资源。
 3. POSITION 不可以和查询条件绑定，可能会出现视图功能应用在不同页面、不同位置、同一查询条件
    的情况，这样的模式会引起混淆管理。
 4. POSITION 只能和配置列表绑定，即配置列表中引入一个 position 来实现不同 position中的
    视图，但此处给了一个基本假设，即列表提供了一个 option 来表明位置信息。

 比如离职员工和在职员工
 视图特性：
 1. 都是员工数据，模型一样：res.employee，即资源地址一致
 2. 都是个人视图，OWNER_TYPE = USER
 3. 都是我的视图，OWNER = 个人用户ID
 4. 开启了视图管理功能，NAME = DEFAULT + 用户管理

 上述四点是目前已经实现的功能

 如何区分一个档案以及一个列表，目前能考虑的方案就是在前端配置中让员工的列表提供位置数据
 离职员工列表：POSITION = HISTORY
 在职员工列表：POSITION = RUNNING
 这样就从模块级区分开了。
 */