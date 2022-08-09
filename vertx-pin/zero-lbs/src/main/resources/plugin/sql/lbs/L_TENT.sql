-- liquibase formatted sql

-- changeset Lang:l-tent-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for L_TENT
-- ----------------------------
DROP TABLE IF EXISTS `L_TENT`;
CREATE TABLE `L_TENT`
(
    `KEY`           VARCHAR(36) NOT NULL COMMENT '「key」- 主键',
    `NAME`          VARCHAR(32) NOT NULL COMMENT '「name」- 名称',
    `CODE`          VARCHAR(36) DEFAULT NULL COMMENT '「code」- 编码',

    `CONTACT_PHONE` VARCHAR(20) DEFAULT NULL COMMENT '「contactPhone」- 联系电话',
    `CONTACT_NAME`  VARCHAR(64) DEFAULT NULL COMMENT '「contactName」- 联系人姓名',

    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `ORDER`         INT(11)     DEFAULT NULL COMMENT '「order」- 排序',

    `LOCATION_ID`   VARCHAR(36) DEFAULT NULL COMMENT '「locationId」- 关联地址ID',
    `YARD_ID`       VARCHAR(36) DEFAULT NULL COMMENT '「yardId」- 关联小区ID',

    -- 特殊字段
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:l-tent-2
ALTER TABLE L_TENT
    ADD UNIQUE (`CODE`, `SIGMA`);

-- changeset Lang:l-tent-3
-- ----------------------------
-- Records of L_TENT
-- ----------------------------
INSERT INTO `L_TENT`
VALUES ('1d9a21ca-7463-43e0-adc3-77076c2f4be6', '3栋', 'T3', NULL, NULL, NULL, 3, '30c5632d-12ed-4044-a602-808784d3b4bf',
        NULL, 1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_TENT`
VALUES ('6beec50f-ecca-4f9a-854e-e1db69f2d47f', '2栋', 'T2', NULL, NULL, NULL, 2, '30c5632d-12ed-4044-a602-808784d3b4bf',
        NULL, 1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_TENT`
VALUES ('d2c971a2-5c43-4baf-8692-c735419c1d13', '1栋', 'T1', NULL, NULL, NULL, 1, '30c5632d-12ed-4044-a602-808784d3b4bf',
        NULL, 1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
