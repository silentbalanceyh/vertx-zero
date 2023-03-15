-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-contract-1
-- ----------------------------
-- Table structure for E_CONTRACT
-- ----------------------------
DROP TABLE IF EXISTS `E_CONTRACT`;
CREATE TABLE `E_CONTRACT`
(
    `KEY`           VARCHAR(36) NOT NULL COMMENT '「key」- 合同主键',
    `NAME`          VARCHAR(255) COMMENT '「name」- 合同名称',
    `CODE`          VARCHAR(255) COMMENT '「code」- 合同编号',

    -- 关联字典
    `DEPT_ID`       VARCHAR(36) COMMENT '「deptId」- 所属部门, resource.departments',
    `TYPE`          VARCHAR(36) COMMENT '「type」- 合同分类，zero.contract',
    `STATUS`        VARCHAR(36) COMMENT '「status」- 合同状态，zero.contract.status',
    `BUDGET`        VARCHAR(36) COMMENT '「budget」- 所属预算，zero.contract.budget',
    `LEVEL`         VARCHAR(36) COMMENT '「level」- 合同级别，zero.contract.level',


    -- 合同基本信息
    `TITLE`         VARCHAR(255) COMMENT '「title」- 合同标题',
    `SUBJECT`       TEXT COMMENT '「subject」- 合同目标',
    `DESCRIPTION`   LONGTEXT COMMENT '「description」- 合同描述',
    `AMOUNT`        DECIMAL(18, 2) COMMENT '「amount」- 合同金额',
    `REMARK`        LONGTEXT COMMENT '「remark」- 合同备注',

    -- 时间
    `PLAN_START_AT` DATETIME COMMENT '「planStartAt」- 开始日期',
    `PLAN_END_AT`   DATETIME COMMENT '「planEndAt」- 结束日期',
    `START_AT`      DATETIME COMMENT '「startAt」- 实际开始日期',
    `END_AT`        DATETIME COMMENT '「endAt」- 实际结束日期',
    `SIGNED_BY`     VARCHAR(36) COMMENT '「signedBy」- 签订人',
    `SIGNED_AT`     DATETIME COMMENT '「signedAt」- 签订时间',

    -- 合同甲乙方
    `PARTY_A`       VARCHAR(36) COMMENT '「partyA」- 甲方（关联公司ID，E_COMPANY）',
    `PARTY_B`       VARCHAR(36) COMMENT '「partyB」- 乙方（关联客户ID，E_CUSTOMER）',

    -- 甲乙丙方信息（丙方为三方合同专用）
    `A_NAME`        VARCHAR(255) COMMENT '「aName」- 甲方名称（个人为姓名/企业为企业名）',
    `A_PHONE`       VARCHAR(20) COMMENT '「aPhone」- 甲方电话',
    `A_LEGAL`       VARCHAR(255) COMMENT '「aLegal」- 甲方法人（企业合同专用）',
    `A_ADDRESS`     TEXT COMMENT '「aAddress」- 甲方地址',

    `B_NAME`        VARCHAR(255) COMMENT '「bName」- 乙方名称（个人为姓名/企业为企业名）',
    `B_PHONE`       VARCHAR(20) COMMENT '「bPhone」- 乙方人电话',
    `B_LEGAL`       VARCHAR(255) COMMENT '「bLegal」- 乙方法人（企业合同专用）',
    `B_ADDRESS`     TEXT COMMENT '「bAddress」- 乙方地址',

    -- 特殊字段
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识（公司所属应用）',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-contract-2
ALTER TABLE E_CONTRACT
    ADD UNIQUE (`CODE`, `SIGMA`);