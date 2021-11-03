-- liquibase formatted sql

-- changeset Lang:p-wh-1
-- 流程定义表：P_WH
DROP TABLE IF EXISTS P_WH;
CREATE TABLE IF NOT EXISTS P_WH
(
    `KEY`              VARCHAR(36) COMMENT '「key」- 仓库主键',
    `NAME`             VARCHAR(255) COMMENT '「name」- 仓库名称',
    `CODE`             VARCHAR(255) COMMENT '「code」- 仓库编号（系统可用）',
    `TYPE`             VARCHAR(36) COMMENT '「type」- 仓库类型',
    `STATUS`           VARCHAR(36) COMMENT '「status」- 仓库状态',
    -- 仓库管理员
    `MANAGER_ID`       VARCHAR(36) COMMENT '「managerId」- 仓库管理员',
    `MANAGER_NAME`     VARCHAR(255) COMMENT '「managerName」- 管理员姓名',
    `MANAGER_PHONE`    VARCHAR(255) COMMENT '「managerPhone」- 管理员电话',
    `MANAGER_MOBILE`   VARCHAR(255) COMMENT '「managerMobile」- 管理员手机',

    -- 仓库特征
    `NEGATIVE`         BIT         DEFAULT NULL COMMENT '「negative」- 允许负库存',
    `SPACE`            BIT         DEFAULT NULL COMMENT '「space」- 仓位管理',
    `AREA_NAME`        TEXT COMMENT '「areaName」- 区域名称（手填）',
    `COMMENT`          TEXT COMMENT '「comment」- 仓库备注',
    `DISTINCT_ID`      VARCHAR(36) COMMENT '「distinctId」- 仓库行政区域',
    `LOCATION_ID`      VARCHAR(36) COMMENT '「locationId」- 启用LBS时对应的Location主键',
    `LOCATION_ADDRESS` TEXT COMMENT '「locationAddress」- 仓库地址',

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
-- changeset Lang:p-wh-2
ALTER TABLE P_WH
    ADD UNIQUE (`CODE`, `SIGMA`); -- 仓库编号不重复