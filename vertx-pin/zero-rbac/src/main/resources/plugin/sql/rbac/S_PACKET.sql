-- liquibase formatted sql

-- changeset Lang:ox-packet-1
-- 权限定义规则专用表，资源路径表：S_PACKET
/**
 * 资源信息描述，用于描述最终的三个维度信息
 * 1）主要用于写操作的最终影响定义
 * 2）和资源本身进行绑定，一个记录关联到一条资源信息（并且在资源管理时不触碰），属于反向连接
 * 3）管理资源下对应的几个不同的维度
 * -- projection 列定义
 * -- rows 行定义
 * -- criteria 条件定义
 * 4）最终通过 S_PACKET -> S_VIEW 流程，S_VIEW 为专用资源窗口
 * -- 在角色层面，专用资源窗口对所有角色生效
 * -- 在用户层面，专用资源窗口优先考虑（高于角色）对单个用户生效
 **/
DROP TABLE IF EXISTS S_PACKET;
CREATE TABLE IF NOT EXISTS S_PACKET
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 包信息',
    `CODE`          VARCHAR(255) COMMENT '「code」- 关联的 PATH 表对应的 code',
    `RESOURCE`      VARCHAR(255) COMMENT '「resource」- 关联的资源表对应的 code',

    -- 当前资源记录影响的维度计算
    /*
     * 针对视图 rows 的变更，rows 的最终格式
     * {
     *     field: [
     *         value1,
     *         value2,
     *         value3,
     *     ]
     * }
     * 行过滤类型
     * - IN:        单字段过滤                   ->      field in ()
     * - AND:       多字段平行过滤（AND连接）      ->      field in () AND field in ()
     * - OR:        多字段平行过滤（OR连接）       ->      field in () OR field in ()
     * - TREE:      多字段复杂过滤                ->      Query Engine
     * 字段配置设置 hConfig
     * {
     *     "field": "STRING | ARRAY",
     *     "input": {
     *     },
     *     "qr": {
     *         "hType = TREE"
     *     }
     * }
     **/
    `H_TYPE`        VARCHAR(16) COMMENT '「hType」- 行过滤类型',
    `H_MAPPING`     LONGTEXT COMMENT '「hMapping」- 字段映射关系，存在转换时必须',
    `H_CONFIG`      LONGTEXT COMMENT '「hConfig」- 字段附加配置',

    /*
     * projection 的变更，列配置
     * [
     *      column1,
     *      column2,
     *      column3
     * ]
     * 列过滤类型
     * - LINEAR:       单列
     * - MULTI:        多列
     * 列字段配置 vConfig
     * {
     *     "field": "STRING | ARRAY",
     *     "input": {
     *     }
     * }
     */
    `V_TYPE`        VARCHAR(16) COMMENT '「vType」- 列过滤类型',
    `V_MAPPING`     LONGTEXT COMMENT '「vMapping」- 列字段映射关系，存在转换时必须',
    `V_CONFIG`      LONGTEXT COMMENT '「vConfig」- 列配置',

    /*
     * criteria 的变更，核心配置
     * Qr过滤类型
     * - FRONT:        前端Qr解析
     * - BACK:         后端Qr解析
     * QR配置 qConfig
     * {
     *     "input": {
     *     }
     * }
     */
    `Q_TYPE`        VARCHAR(16) COMMENT '「qType」- 条件模板',
    `Q_MAPPING`     LONGTEXT COMMENT '「qMapping」- 查询条件映射关系',
    `Q_CONFIG`      LONGTEXT COMMENT '「qConfig」- 条件配置（界面配置相关）',

    -- 保留设置（扩展自定义配置）
    `RUN_COMPONENT` VARCHAR(255) COMMENT '「runComponent」- 自定义模式下的组件',
    `RUN_CONFIG`    LONGTEXT COMMENT '「runConfig」- 运行专用配置',
    -- 访问者定义（绑定的 resource 中 virtual = true）
    -- 当前资源记录影响的视图计算，和资源访问语法相同
    `SEEK_SYNTAX`   LONGTEXT COMMENT '「seekSyntax」- 访问者语法',
    `SEEK_CONFIG`   LONGTEXT COMMENT '「seekConfig」- 访问者配置',

    `SIGMA`         VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`        BIT COMMENT '「active」- 是否启用',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-packet-2
-- Unique Key：独立唯一主键
ALTER TABLE S_PACKET
    ADD UNIQUE (`CODE`, `RESOURCE`, `SIGMA`);
ALTER TABLE S_PACKET
    ADD INDEX IDX_S_PACKET_PATH_CODE_SIGMA (`CODE`, `SIGMA`);