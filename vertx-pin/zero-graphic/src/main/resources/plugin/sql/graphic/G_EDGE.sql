-- liquibase formatted sql

-- changeset Lang:ox-edge-1
-- 拓扑图中的边
DROP TABLE IF EXISTS G_EDGE;
CREATE TABLE IF NOT EXISTS G_EDGE
(
    `KEY`         VARCHAR(36) COMMENT '「key」- 拓扑图边的ID',
    `NAME`        VARCHAR(255) COMMENT '「name」- 边的名称',
    `TYPE`        VARCHAR(36) COMMENT '「type」- 拓扑图边的类型，描述关系本质',

    -- 图属性
    `SOURCE_KEY`  VARCHAR(36) COMMENT '「sourceKey」- 拓扑图开始节点',
    `TARGET_KEY`  VARCHAR(36) COMMENT '「targetKey」- 拓扑图结束节点',
    `GRAPHIC_ID`  VARCHAR(36) COMMENT '「graphicId」- 当前关联拓扑图ID',
    `UI`          TEXT COMMENT '「ui」- ui配置专用',
    `RECORD_KEY`  VARCHAR(36) COMMENT '「recordKey」- 记录主键',
    `RECORD_DATA` TEXT COMMENT '「recordData」- 组中存储的数据信息',

    -- 特殊字段
    `SIGMA`       VARCHAR(32) COMMENT '「sigma」- 统一标识',
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
ALTER TABLE G_EDGE
    ADD UNIQUE (`NAME`, `GRAPHIC_ID`) USING BTREE;