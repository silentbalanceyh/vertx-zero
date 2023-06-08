-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-company-1
-- ----------------------------
-- Table structure for E_COMPANY
-- ----------------------------
DROP TABLE IF EXISTS `E_COMPANY`;
CREATE TABLE `E_COMPANY`
(
    `KEY`               VARCHAR(36) NOT NULL COMMENT '「key」- 公司主键',
    `NAME`              VARCHAR(255) COMMENT '「name」- 公司名称',
    `ALIAS`             VARCHAR(255) COMMENT '「alias」- 公司别称',
    `TITLE`             VARCHAR(255) COMMENT '「title」- 公司显示标题',
    `CODE`              VARCHAR(255) COMMENT '「code」- 公司编号',
    `COMMENT`           TEXT COMMENT '「comment」- 公司简介',

    `TAX_CODE`          VARCHAR(255) COMMENT '「taxCode」- 公司税号',
    `TAX_TITLE`         VARCHAR(255) COMMENT '「taxTitle」- 开票抬头',

    -- 公司业务信息
    `EMAIL`             VARCHAR(255) COMMENT '「email」- 企业邮箱',
    `FAX`               VARCHAR(255) COMMENT '「fax」- 传真号',
    `HOMEPAGE`          VARCHAR(128) COMMENT '「homepage」- 公司主页',
    `LOGO`              VARCHAR(36) COMMENT '「logo」- 附件对应的 attachment Key',
    `PHONE`             VARCHAR(20) COMMENT '「phone」- 公司座机',
    `ADDRESS`           TEXT COMMENT '「address」- 公司地址',
    `LOCATION_ID`       VARCHAR(36) COMMENT '「locationId」- 启用LBS时对应的Location主键',

    -- 企业法人
    `CORPORATION_NAME`  VARCHAR(255) COMMENT '「corporationName」- 企业法人',
    `CORPORATION_PHONE` VARCHAR(255) COMMENT '「corporationPhone」- 法人电话',

    -- 联系人
    `CONTACT_NAME`      VARCHAR(255) COMMENT '「contactName」- 联系人电话',
    `CONTACT_PHONE`     VARCHAR(20) COMMENT '「contactPhone」- 联系人电话',
    `CONTACT_ONLINE`    VARCHAR(255) COMMENT '「contactOnline」- 在线联系方式',

    `COMPANY_ID`        VARCHAR(36) COMMENT '「companyId」- 公司、子公司结构时需要',
    `CUSTOMER_ID`       VARCHAR(36) COMMENT '「customerId」- 公司作为客户时的客户信息',

    -- 特殊字段
    `TYPE`              VARCHAR(36) COMMENT '「type」- 公司分类',
    `METADATA`          TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`            BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`             VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识（公司所属应用）',
    `LANGUAGE`          VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`        DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`        VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`        DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`        VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-company-2
ALTER TABLE E_COMPANY
    ADD UNIQUE (`CODE`, `SIGMA`) USING BTREE;
ALTER TABLE E_COMPANY
    ADD UNIQUE (`NAME`, `SIGMA`) USING BTREE;
ALTER TABLE E_COMPANY
    ADD UNIQUE (`TAX_CODE`, `SIGMA`) USING BTREE;
ALTER TABLE E_COMPANY
    ADD UNIQUE (`CUSTOMER_ID`, `SIGMA`) USING BTREE; -- 当一个公司作为客户时，它的客户信息是唯一的，1:1 的关系