-- liquibase formatted sql

-- changeset Lang:l-location-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for L_LOCATION
-- ----------------------------
DROP TABLE IF EXISTS `L_LOCATION`;
CREATE TABLE `L_LOCATION`
(
    `KEY`        VARCHAR(36)  NOT NULL COMMENT '「key」- 主键',
    `NAME`       VARCHAR(32)  NOT NULL COMMENT '「name」- 名称',
    `CODE`       VARCHAR(36) DEFAULT NULL COMMENT '「code」- 编码',

    `ADDRESS`    TEXT        DEFAULT NULL COMMENT '「address」- 详细地址',
    `CITY`       VARCHAR(32) DEFAULT NULL COMMENT '「city」- 3.城市',
    `COUNTRY`    VARCHAR(32) DEFAULT NULL COMMENT '「country」- 1.国家',
    `REGION`     VARCHAR(32) DEFAULT NULL COMMENT '「region」- 4.区域',
    `FULL_NAME`  VARCHAR(255) NOT NULL COMMENT '「fullName」- 地址全称',
    `STATE`      VARCHAR(32) DEFAULT NULL COMMENT '「state」- 2.省会',
    `STREET1`    VARCHAR(72) DEFAULT NULL COMMENT '「street1」- 街道1',
    `STREET2`    VARCHAR(72) DEFAULT NULL COMMENT '「street2」- 街道2',
    `STREET3`    VARCHAR(72) DEFAULT NULL COMMENT '「street3」- 街道3',
    `POSTAL`     VARCHAR(16) DEFAULT NULL COMMENT '「postal」- 邮政编码',

    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `REGION_ID`  VARCHAR(36)  NOT NULL COMMENT '「regionId」- 区域ID',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:l-location-2
ALTER TABLE L_LOCATION
    ADD UNIQUE (`CODE`, `SIGMA`);
-- changeset Lang:l-location-3

-- ----------------------------
-- Records of L_LOCATION
-- ----------------------------
INSERT INTO `L_LOCATION`
VALUES ('217e8162-c0e4-4294-b798-dbd18dd31917', '供应商地址2', 'CN.CQ.LCT.VENDOR2', '奥园康城A区10号楼8栋13-5供应商2', '重庆市', '中国',
        '渝中区', '重庆市渝中区黄杨路奥园康城A区10号楼8栋13-5供应商2', '重庆', '黄杨路', NULL, NULL, '400040', NULL,
        '374889fc-1c4b-4260-a351-18e5220a2a54', 1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL,
        '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_LOCATION`
VALUES ('30c5632d-12ed-4044-a602-808784d3b4bf', '默认居住地址', 'CN.CQ.LCT.DEFAULT', '奥园康城A区10号楼8栋13-5', '重庆市', '中国', '渝中区',
        '重庆市渝中区黄杨路奥园康城A区10号楼8栋13-5', '重庆', '黄杨路', NULL, NULL, '400040', NULL, '374889fc-1c4b-4260-a351-18e5220a2a54', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_LOCATION`
VALUES ('d3678a79-705b-45ed-8193-af21efd1e92b', '供应商地址1', 'CN.CQ.LCT.VENDOR', '奥园康城A区10号楼8栋13-5供应商', '重庆市', '中国', '渝中区',
        '重庆市渝中区黄杨路奥园康城A区10号楼8栋13-5供应商', '重庆', '黄杨路', NULL, NULL, '400040', NULL, '374889fc-1c4b-4260-a351-18e5220a2a54',
        1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
