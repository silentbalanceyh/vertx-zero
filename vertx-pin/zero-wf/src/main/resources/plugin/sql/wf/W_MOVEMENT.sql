-- liquibase formatted sql

-- changeset Lang:w-movement-1
-- 节点定义表：W_MOVEMENT
DROP TABLE IF EXISTS W_MOVEMENT;
CREATE TABLE IF NOT EXISTS W_MOVEMENT
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 轨迹主键',
    `CODE`             VARCHAR(255) COMMENT '「code」- 轨迹编号（系统可用）',
    `NAME`             VARCHAR(255) COMMENT '「name」- 流程定义名称',

    -- 关联图/表单 基本信息
    `SOURCE_KEY`       VARCHAR(36) COMMENT '「sourceKey」',
    `TARGET_KEY`       VARCHAR(36) COMMENT '「targetKey」',
    `FLOW_INSTANCE_ID` VARCHAR(36) COMMENT '「flowInstanceId」- 所属流程实例ID',

    -- 执行信息
    `MOVE_NAME`        VARCHAR(255) COMMENT '「moveName」- 执行人姓名',
    `MOVE_BY`          VARCHAR(36) COMMENT '「moveBy」- 执行人员',
    `MOVE_AT`          DATETIME COMMENT '「moveAt」- 执行时间',
    `MOVE_COMPONENT`   TEXT COMMENT '「moveComponent」- 执行扩展组件',

    -- 特殊字段
    `ACTIVE`           BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`            VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`         TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`         VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`       DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`       VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`       DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`       VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:w-movement-2
ALTER TABLE W_MOVEMENT
    ADD UNIQUE (`CODE`, `SIGMA`); -- 流程定义编码不重复