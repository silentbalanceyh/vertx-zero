-- liquibase formatted sql

-- changeset Lang:ox-join-1
-- 实体模型关联图 多对多
-- 保存了内部关系，非模型和模型之间，而是 Model -> Set<Entity> 之间的关系
-- 处理 Join类型，模型的主键依靠运算，直接通过当前 NEXUS 内部关系表来执行计算
DROP TABLE IF EXISTS M_JOIN;
CREATE TABLE IF NOT EXISTS M_JOIN
(
    `MODEL`      VARCHAR(32) COMMENT '「model」- 模型identifier',
    `ENTITY`     VARCHAR(32) COMMENT '「entity」- 实体identifier',
    `ENTITY_KEY` VARCHAR(32) COMMENT '「entityKey」- 实体主键字段名',
    `PRIORITY`   INT DEFAULT 0 COMMENT '「priority」- 优先级',
    `NAMESPACE`  VARCHAR(64) COMMENT '「namespace」- 名空间（和App绑定的）',
    PRIMARY KEY
        (
         `MODEL`,
         `ENTITY`,
         `ENTITY_KEY`,
         `NAMESPACE`
            ) USING BTREE
);
ALTER TABLE M_JOIN
    ADD INDEX IDXM_M_JOIN_NAMESPACE_MODEL (`NAMESPACE`, `MODEL`) USING BTREE;