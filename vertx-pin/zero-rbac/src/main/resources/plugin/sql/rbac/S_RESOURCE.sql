-- liquibase formatted sql

-- changeset Lang:ox-resource-1
-- 资源表：S_RESOURCE
DROP TABLE IF EXISTS S_RESOURCE;
CREATE TABLE IF NOT EXISTS S_RESOURCE
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 资源对应的ID',
    `CODE`           VARCHAR(255) COMMENT '「code」- 资源编号',
    `NAME`           VARCHAR(255) COMMENT '「name」- 资源名称',
    `TYPE`           VARCHAR(60) COMMENT '「type」- 资源分类',
    `IDENTIFIER`     VARCHAR(255) COMMENT '「identifier」- 当前资源所属的Model的标识',
    `COMMENT`        TEXT COMMENT '「comment」- 备注信息',

    -- 资源属性（正向查询专用）
    `LEVEL`          INTEGER COMMENT '「level」- 资源需求级别',

    -- 计算Profile专用（资源所需的Profile详细信息）=
    `MODE_ROLE`      VARCHAR(32) COMMENT '「modeRole」- 该资源查找角色的模式',
    `MODE_GROUP`     VARCHAR(32) COMMENT '「modeGroup」- 该资源查找组的模式',
    `MODE_TREE`      VARCHAR(32) COMMENT '「modeTree」- 该资源处理树（用户组）的模式',

    -- 资源访问者配置，动态资源定义
    `VIRTUAL`        BIT COMMENT '「virtual」- 虚拟资源',
    /*
     * 访问者扩展：针对 seekConfig 进行分类，分类位于配置中详细定义
     * 访问者类型是抽象过程中单独定义
     */
    `SEEK_SYNTAX`    LONGTEXT COMMENT '「seekSyntax」- 访问者语法',
    `SEEK_CONFIG`    LONGTEXT COMMENT '「seekConfig」- 访问者配置',
    `SEEK_COMPONENT` VARCHAR(255) COMMENT '「seekComponent」- 访问者组件',

    -- 资源标识
    `SIGMA`          VARCHAR(32) COMMENT '「sigma」- 统一标识',

    -- 特殊字段
    `CATEGORY`       VARCHAR(36) COMMENT '「category」- 资源分类',
    `LANGUAGE`       VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`         BIT COMMENT '「active」- 是否启用',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-resource-2
-- Unique Key：独立唯一主键，同一个Sigma中的资源code不可重复
ALTER TABLE S_RESOURCE
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;