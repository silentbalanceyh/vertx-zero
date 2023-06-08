-- liquibase formatted sql

-- changeset Lang:l-yard-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for L_YARD
-- ----------------------------
DROP TABLE IF EXISTS `L_YARD`;
CREATE TABLE `L_YARD`
(
    `KEY`         VARCHAR(36) NOT NULL COMMENT '「key」- 主键',
    `NAME`        VARCHAR(32) NOT NULL COMMENT '「name」- 名称',
    `CODE`        VARCHAR(36) DEFAULT NULL COMMENT '「code」- 编码',
    `METADATA`    TEXT COMMENT '「metadata」- 附加配置',
    `ORDER`       INT(11)     DEFAULT NULL COMMENT '「order」- 排序',

    `LOCATION_ID` VARCHAR(36) COMMENT '「locationId」- 关联地址ID',

    -- 特殊字段
    `ACTIVE`      BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`       VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`    VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`  DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`  VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`  DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`  VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:l-yard-2
ALTER TABLE L_YARD
    ADD UNIQUE (`CODE`, `SIGMA`);

SET FOREIGN_KEY_CHECKS = 1;
