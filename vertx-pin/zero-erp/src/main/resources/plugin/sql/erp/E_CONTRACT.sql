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
    `KEY`         VARCHAR(36) NOT NULL COMMENT '「key」- 合同主键',
    `NAME`        VARCHAR(255) COMMENT '「name」- 合同名称',
    `CODE`        VARCHAR(255) COMMENT '「code」- 合同编号',
    `TITLE`       VARCHAR(255) COMMENT '「title」- 合同标题',
    `FILE_KEY`    VARCHAR(36) COMMENT '「fileKey」- 合同附件Key',
    -- 金额信息
    `AMOUNT`      DECIMAL(18, 2) COMMENT '「amount」- 合同金额',

    -- 合同甲乙方
    `COMPANY_ID`  VARCHAR(36) COMMENT '「companyId」- 合同甲方',
    `CUSTOMER_ID` VARCHAR(36) COMMENT '「customerId」- 合同乙方',

    -- 过期时间
    `EXPIRED_AT`  DATETIME COMMENT '「expiredAt」- 过期时间',
    `SIGNED_AT`   DATETIME COMMENT '「signedAt」- 签订时间',
    `RUN_AT`      DATETIME COMMENT '「runAt」- 生效时间',

    /*
     * 合同中的特殊属性
     */
    `RUN_UP_AT`   DATETIME COMMENT '「runUpAt」- 挂账到期时间',

    -- 甲乙方信息
    `A_NAME`      VARCHAR(255) COMMENT '「aName」- 甲方签订人',
    `A_PHONE`     VARCHAR(20) COMMENT '「aPhone」- 甲方签订人姓名',
    `A_ADDRESS`   TEXT COMMENT '「aAddress」- 甲方联系地址',

    `B_NAME`      VARCHAR(255) COMMENT '「bName」- 乙方签订人',
    `B_PHONE`     VARCHAR(20) COMMENT '「bPhone」- 乙方签订人姓名',
    `B_ADDRESS`   TEXT COMMENT '「bAddress」- 乙方联系地址',

    -- 特殊字段
    `TYPE`        VARCHAR(36) COMMENT '「type」- 合同分类',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识（公司所属应用）',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-contract-2
ALTER TABLE E_CONTRACT
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;