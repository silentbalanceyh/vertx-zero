-- liquibase formatted sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- changeset Lang:h-identity-1
-- ----------------------------
-- Table structure for E_IDENTITY
-- ----------------------------
-- 证明个人身份
DROP TABLE IF EXISTS `E_IDENTITY`;
CREATE TABLE `E_IDENTITY`
(
    `KEY`             VARCHAR(36) NOT NULL COMMENT '「key」- 身份主键',
    `CODE`            VARCHAR(255) COMMENT '「code」- 系统编号',
    `TYPE`            VARCHAR(36) COMMENT '「type」- 身份类型/档案类型',
    `STATUS`          VARCHAR(255) COMMENT '「status」- 档案状态',

    -- 人的基本信息
    `COUNTRY`         VARCHAR(20) COMMENT '「country」- 国籍',
    `NATIVE_PLACE`    VARCHAR(255) COMMENT '「nativePlace」- 籍贯',
    `NATION`          VARCHAR(32) COMMENT '「nation」- 民族',
    `ADDRESS`         TEXT COMMENT '「address」- 居住地址',
    `REALNAME`        VARCHAR(255) COMMENT '「realname」- 真实姓名',
    `BIRTHDAY`        DATETIME COMMENT '「birthday」- 生日',
    `DRIVER_LICENSE`  VARCHAR(255) COMMENT '「driverLicense」- 驾驶证',
    `CAR_PLATE`       VARCHAR(255) COMMENT '「carPlate」- 常用车牌',
    `PASSPORT`        VARCHAR(255) COMMENT '「passport」- 护照',
    `GENDER`          BIT COMMENT '「gender」- 性别',
    `MARITAL`         VARCHAR(32) COMMENT '「marital」- 婚姻状况',

    -- 联系方式
    `CONTACT_MOBILE`  VARCHAR(20) COMMENT '「contactMobile」- 联系手机',
    `CONTACT_PHONE`   VARCHAR(20) COMMENT '「contactPhone」- 座机',
    `CONTACT_ADDRESS` TEXT COMMENT '「contactAddress」- 联系地址',
    `CONTACT_EMAIL`   VARCHAR(255) COMMENT '「contactEmail」- 联系Email',

    `URGENT_NAME`     VARCHAR(255) COMMENT '「urgentName」- 紧急联系人',
    `URGENT_PHONE`    VARCHAR(255) COMMENT '「urgentPhone」- 紧急联系电话',

    -- 电子商务身份
    `EC_QQ`           VARCHAR(64) COMMENT '「ecQq」- QQ号码',
    `EC_ALIPAY`       VARCHAR(64) COMMENT '「ecAlipay」- 支付宝',
    `EC_WECHAT`       VARCHAR(64) COMMENT '「ecWechat」- 微信',

    -- 证件信息
    `IDC_TYPE`        VARCHAR(36) COMMENT '「idcType」- 证件类型',
    `IDC_NUMBER`      VARCHAR(255) COMMENT '「idcNumber」- 证件号',
    `IDC_ADDRESS`     TEXT COMMENT '「idcAddress」- 证件地址',
    `IDC_EXPIRED_AT`  DATETIME COMMENT '「idcExpiredAt」- 证件过期时间',
    `IDC_FRONT`       VARCHAR(36) COMMENT '「idcFront」- 证件正面附件',
    `IDC_BACK`        VARCHAR(36) COMMENT '「idcBack」- 证件背面附件',
    `IDC_ISSUER`      VARCHAR(255) COMMENT '「idcIssuer」- 证件签发机构',
    `IDC_ISSUE_AT`    DATETIME COMMENT '「idcIssueAt」- 证件签发时间',

    -- 证件状态
    `VERIFIED`        BIT COMMENT '「verified」- 是否验证、备案',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置',
    `ACTIVE`          BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`           VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`        VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);
-- changeset Lang:h-identity-2
ALTER TABLE E_IDENTITY
    ADD UNIQUE (`TYPE`, `IDC_TYPE`, `IDC_NUMBER`) USING BTREE;