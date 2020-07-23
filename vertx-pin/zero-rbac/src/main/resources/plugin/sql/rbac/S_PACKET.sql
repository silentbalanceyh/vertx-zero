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
    `KEY`        VARCHAR(36) COMMENT '「key」- 包信息',
    /*
     * 触发类型可能是多种，不同触发类型的配置信息一致
     * projection
     * rows
     * criteria
     **/
    `PATH_ID`           VARCHAR(36) COMMENT '「pathId」- 关联的 path id，包含关系',
    `RESOURCE_ID`       VARCHAR(36) COMMENT '「resourceId」- 关联的资源 id',

     /*
      * rows 的变更，它的格式如：
      * {
            field: [
                value1,
                value2,
                value3
            ]
      * }
      * 行类型
      * SINGLE - 单字段过滤
      * COMPLEX - 多字段复杂过滤（保留）
      **/
    `ROW_TYPE`          VARCHAR(255) COMMENT '「rowType」- 行过滤类型',
    `ROW_FIELD`         VARCHAR(255) COMMENT '「rowField」- 行输入',
    `ROW_TPL`           TEXT COMMENT '「rowTpl」- 多字段的模板',
    `ROW_TPL_MAPPING`   TEXT COMMENT '「rowTplMapping」- 多字段的映射关系',

    /*
     * projection 的变更，列配置
     * [
            column1,
            column2,
            column3
     * ]
     * LINER - 单字段列（一维）
     */
     `COL_TYPE`         VARCHAR(255) COMMENT '「colType」- 列过滤类型',
     `COL_CONFIG`       TEXT COMMENT '「colConfig」- 列配置',

     /*
      * criteria 的变更，核心配置
      */
     `COND_TPL`             TEXT COMMENT '「condTpl」- 条件模板',
     `COND_TPL_MAPPING`     TEXT COMMENT '「condTplMapping」- 查询条件映射关系',

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

-- changeset Lang:ox-packet-2
-- Unique Key：独立唯一主键
ALTER TABLE S_PACKET
    ADD UNIQUE (`PATH_ID`,`RESOURCE_ID`,`SIGMA`);
ALTER TABLE S_PACKET ADD
    INDEX IDX_S_PACKET_PATH_ID (`PATH_ID`);