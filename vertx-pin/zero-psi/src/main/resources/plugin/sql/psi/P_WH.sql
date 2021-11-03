-- liquibase formatted sql

-- changeset Lang:p-wh-1
-- 流程定义表：P_WH
DROP TABLE IF EXISTS P_WH;
CREATE TABLE IF NOT EXISTS P_WH
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 仓库主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 仓库名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 仓库编号（系统可用）',
    `TYPE`       VARCHAR(36) COMMENT '「type」- 仓库类型',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-wh-2
ALTER TABLE P_WH
    ADD UNIQUE (`NAME`, `SIGMA`); -- 流程定义名称不重复