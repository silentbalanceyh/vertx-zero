-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-customer-1
-- ----------------------------
-- Table structure for E_CUSTOMER
-- ----------------------------
-- 客户表：
-- - Vendor：供应商
-- - Partner：合作伙伴
-- - Member：会员
DROP TABLE IF EXISTS `E_CUSTOMER`;
CREATE TABLE `E_CUSTOMER`
(
    `KEY`            VARCHAR(36) NOT NULL COMMENT '「key」- 客户ID',
    `NAME`           VARCHAR(255) COMMENT '「name」- 客户名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 客户编号',
    `TYPE`           VARCHAR(36) COMMENT '「type」- 客户分类（不同类型代表不同客户）',
    `STATUS`         VARCHAR(36) COMMENT '「status」- 客户状态',

    -- 税务相关信息
    `TAX_CODE`       VARCHAR(255) COMMENT '「taxCode」- 税号',
    `TAX_TITLE`      VARCHAR(255) COMMENT '「taxTitle」- 开票抬头',

    -- 联系人
    `CONTACT_NAME`   VARCHAR(255) COMMENT '「contactName」- 联系人姓名',
    `CONTACT_PHONE`  VARCHAR(20) COMMENT '「contactPhone」- 联系人电话',
    `CONTACT_EMAIL`  VARCHAR(255) COMMENT '「contactEmail」- 联系人Email',
    `CONTACT_ONLINE` VARCHAR(255) COMMENT '「contactOnline」- 在线联系方式',

    -- 客户基本信息
    `TITLE`          VARCHAR(255) COMMENT '「title」- 客户显示标题',
    `COMMENT`        TEXT COMMENT '「comment」- 客户备注',
    `EMAIL`          VARCHAR(255) COMMENT '「email」- 企业邮箱',
    `FAX`            VARCHAR(255) COMMENT '「fax」- 传真号',
    `HOMEPAGE`       VARCHAR(128) COMMENT '「homepage」- 客户主页',
    `LOGO`           VARCHAR(36) COMMENT '「logo」- 附件对应的 attachment Key',
    `PHONE`          VARCHAR(20) COMMENT '「phone」- 客户座机',
    `ADDRESS`        TEXT COMMENT '「address」- 客户地址',

    -- 签单人/挂账属性
    `SIGN_NAME`      VARCHAR(255) COMMENT '「signName」- 签单人姓名',
    `SIGN_PHONE`     VARCHAR(20) COMMENT '「signPhone」- 签单人电话',
    `RUN_UP`         BIT            DEFAULT NULL COMMENT '「runUp」- 挂账属性',
    `RUN_UP_AMOUNT`  DECIMAL(18, 2) DEFAULT NULL COMMENT '「runUpAmount」- 挂账限额',

    -- 银行账号/开户行
    `BANK_ID`        VARCHAR(36) COMMENT '「bankId」- 开户行',
    `BANK_CARD`      VARCHAR(255) COMMENT '「bankCard」- 开户行账号',

    -- 特殊字段
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`         BIT            DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32)    DEFAULT NULL COMMENT '「sigma」- 统一标识（客户所属应用）',
    `LANGUAGE`       VARCHAR(8)     DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:h-customer-2
ALTER TABLE E_CUSTOMER
    ADD UNIQUE (`TAX_CODE`, `SIGMA`) USING BTREE;