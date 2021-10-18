-- liquibase formatted sql

-- changeset Lang:ox-element-1
-- 拓扑图中的点专用表：G_NODE
DROP TABLE IF EXISTS G_NODE;
CREATE TABLE IF NOT EXISTS G_NODE
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 节点ID',
    `NAME`             VARCHAR(255) COMMENT '「name」- 节点呈现名称',
    `X`                DECIMAL(18, 6) COMMENT '「x」- 当前节点在图上的x坐标',
    `Y`                DECIMAL(18, 6) COMMENT '「y」- 当前节点在图上的y坐标',
    `UI`               TEXT COMMENT '「ui」- ui配置专用',

    -- 组名称
    `GRAPHIC_ID`       VARCHAR(36) COMMENT '「graphicId」- 它所关联的图实例ID',
    `RECORD_DATA`      TEXT COMMENT '「recordData」- 该节点存储的数据信息',
    `RECORD_KEY`       VARCHAR(36) COMMENT '「recordKey」- 记录主键',
    `RECORD_COMPONENT` VARCHAR(255) COMMENT '「recordComponent」- 记录处理组件',
    `RECORD_CLASS`     VARCHAR(255) COMMENT '「recordClass」- 记录绑定Pojo类型',

    -- 特殊字段
    `SIGMA`            VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`         VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`           BIT COMMENT '「active」- 是否启用',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
ALTER TABLE G_NODE
    ADD UNIQUE (`NAME`, `GRAPHIC_ID`) USING BTREE;