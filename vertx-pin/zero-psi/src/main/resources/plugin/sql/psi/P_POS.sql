-- liquibase formatted sql

-- changeset Lang:p-pos-1
-- 流程定义表：P_POS
DROP TABLE IF EXISTS P_POS;
CREATE TABLE IF NOT EXISTS P_POS
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 仓位主键',
    `NAME`            VARCHAR(255) COMMENT '「name」- 仓位名称',
    `CODE`            VARCHAR(255) COMMENT '「code」- 仓位编号（系统可用）',
    `TYPE`            VARCHAR(36) COMMENT '「type」- 仓位类型',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 仓位状态',

    `WH_ID`           VARCHAR(36) COMMENT '「whId」- 所属仓库信息',
    -- 仓位特性
    `DIRECT`          BIT         DEFAULT NULL COMMENT '「direct」- 直接仓位，1对1',
    `CAPACITY`        BIGINT      DEFAULT 0 COMMENT '「capacity」- 仓位容量',
    `CAPACITY_EXCEED` BIGINT      DEFAULT 0 COMMENT '「capacityExceed」- 仓位超容部分',
    `LIMIT_TYPE`      TEXT COMMENT '「limitType」- 仓位类型限制',
    `LIMIT_RULE`      TEXT COMMENT '「limitRule」- 仓位限制规则',

    -- 仓位位置特性
    `POS_ROW`         INTEGER     DEFAULT NULL COMMENT '「posRow」- 行维度',
    `POS_COLUMN`      INTEGER     DEFAULT NULL COMMENT '「posColumn」- 列维度',
    `POS_HEIGHT`      INTEGER     DEFAULT NULL COMMENT '「posHeight」- 高维度',
    `POS_TAGS`        TEXT COMMENT '「posTags」- 标签，横切维度位置管理',
    `POS_TRACE`       TEXT COMMENT '「posTrace」- 空间位置描述',
    `COMMENT`         TEXT COMMENT '「comment」- 仓库备注',

    -- 特殊字段
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-pos-2
ALTER TABLE P_POS
    ADD UNIQUE (`CODE`, `SIGMA`, `WH_ID`); -- 仓库编号不重复