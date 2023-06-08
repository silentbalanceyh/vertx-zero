-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-dept-1
-- ----------------------------
-- Table structure for E_DEPT
-- ----------------------------
DROP TABLE IF EXISTS `E_DEPT`;
CREATE TABLE `E_DEPT`
(
    `KEY`          VARCHAR(36) NOT NULL COMMENT '「key」- 部门主键',
    `NAME`         VARCHAR(255) COMMENT '「name」- 部门名称',
    `CODE`         VARCHAR(255) COMMENT '「code」- 部门编号',

    -- 部门经理
    `MANAGER_ID`   VARCHAR(36) COMMENT '「managerId」- 部门经理',
    `MANAGER_NAME` VARCHAR(255) COMMENT '「managerName」- 部门名称',
    `COMPANY_ID`   VARCHAR(36) COMMENT '「companyId」- 所属公司',
    `DEPT_ID`      VARCHAR(36) COMMENT '「deptId」- 父部门',
    `COMMENT`      TEXT COMMENT '「comment」- 部门备注',

    -- 特殊字段
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`       BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`        VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`     VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-dept-2
ALTER TABLE E_DEPT
    ADD UNIQUE (`CODE`, `COMPANY_ID`) USING BTREE;
ALTER TABLE E_DEPT
    ADD UNIQUE (`NAME`, `COMPANY_ID`, `DEPT_ID`) USING BTREE;

ALTER TABLE E_DEPT
    ADD INDEX IDX_E_DEPT_SIGMA (`SIGMA`) USING BTREE;
ALTER TABLE E_DEPT
    ADD INDEX IDX_E_DEPT_SIGMA_ACTIVE (`SIGMA`, `ACTIVE`) USING BTREE;