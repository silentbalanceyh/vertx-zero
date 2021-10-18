-- liquibase formatted sql

-- changeset Lang:ox-cluster-node-1
-- 关联表：R_CLUSTER_NODE
DROP TABLE IF EXISTS R_CLUSTER_NODE;
CREATE TABLE IF NOT EXISTS R_CLUSTER_NODE
(
    `CLUSTER_ID` VARCHAR(36) COMMENT '「clusterId」- 组ID',
    `NODE_ID`    VARCHAR(36) COMMENT '「nodeId」- 节点ID',
    PRIMARY KEY
        (
         `CLUSTER_ID`,
         `NODE_ID`
            ) USING BTREE
);