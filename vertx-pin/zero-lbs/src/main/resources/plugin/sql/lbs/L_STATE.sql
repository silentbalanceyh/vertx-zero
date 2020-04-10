-- liquibase formatted sql

-- changeset Lang:l-state-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for L_STATE
-- ----------------------------
DROP TABLE IF EXISTS `L_STATE`;
CREATE TABLE `L_STATE`
(
    `KEY`        VARCHAR(36) NOT NULL COMMENT '「key」- 省会主键',
    `NAME`       VARCHAR(32) NOT NULL COMMENT '「name」- 省会名称',
    `CODE`       VARCHAR(36) DEFAULT NULL COMMENT '「code」- 省会编码',
    `ALIAS`      VARCHAR(32) NOT NULL COMMENT '「alias」- 别名（缩写）',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',

    `ORDER`      INT(11)     DEFAULT NULL COMMENT '「order」- 排序',
    `COUNTRY_ID` VARCHAR(36) NOT NULL COMMENT '「countryId」- 国家ID',

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

-- changeset Lang:l-state-2
ALTER TABLE L_STATE
    ADD UNIQUE (`CODE`, `SIGMA`);

-- changeset Lang:l-state-3
-- ----------------------------
-- Records of L_STATE
-- ----------------------------
BEGIN;
INSERT INTO `L_STATE`
VALUES ('0b9f22d0-5c63-44aa-ad88-469af891fd96', '青海省', 'QH', '青', NULL, 29, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('0e600cd7-eb9b-4f91-8772-96eee4043deb', '天津市', 'TJ', '津', NULL, 5, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('1217c9af-748a-4b42-a120-23f89f79bea0', '海南省', 'HAN', '琼', NULL, 23, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('1929cb4b-3475-4a0f-bf80-bb118b0af8b9', '江西省', 'JX', '赣', NULL, 22, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('1d0d0600-479b-418e-be8c-3eb615b39a3d', '湖南省', 'HUN', '湘', NULL, 19, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('21a4d8bb-76d8-4dcb-b317-00ff122cff6d', '宁夏省', 'NX', '宁', NULL, 30, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('26d8b6aa-81ec-4e94-8920-d7a36b63aa0d', '云南省', 'YN', '滇', NULL, 25, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('2e931610-54aa-4dc4-9f29-9b9a300778c0', '陕西省', 'SX1', '秦', NULL, 27, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('2faaa422-5236-4d12-a27b-abfbba1ef767', '山西省', 'SX', '晋', NULL, 7, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('36043ac3-9d21-4010-877d-f47338210b63', '贵州省', 'GZ', '黔', NULL, 24, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('396fd2ae-3194-4a75-9145-bbd81bee07f2', '吉林省', 'JL', '吉', NULL, 10, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('40f427b4-ccc4-4967-8ce4-f9b9023d24b7', '西藏', 'XZ', '藏', NULL, 26, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('46cdadf4-c40f-4904-9e7a-b5aeb691d627', '台湾省', 'TW', '宝岛', NULL, 32, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('4aab8599-afb8-4552-82d5-5ff97dc7b780', '河北省', 'HEB', '冀', NULL, 6, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('58f94c9d-5db7-4a98-92da-13b8bdea3e1a', '辽宁省', 'LN', '辽', NULL, 9, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('703913de-6889-4224-b817-077aa75aed29', '新疆省', 'XJ', '新', NULL, 31, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('7bb2549c-c713-4012-a420-98ef94687b76', '上海市', 'SH', '泸', NULL, 4, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('8379b422-0672-48b5-a879-2a2250e53088', '浙江省', 'ZJ', '浙', NULL, 16, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('84d6d546-171d-43cc-92a1-fb35022c3dd9', '河南省', 'HEN', '豫', NULL, 8, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('85e12347-dc0c-40a3-ba7d-531c718ca98d', '港澳', 'GA', '香港澳门', NULL, 33, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('8e7f4270-162a-4737-941a-f6ef2b6b580b', '山东省', 'SD', '鲁', NULL, 14, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('9160df58-c1dc-4c78-a774-709d4e3c12f7', '福建省', 'FJ', '闽', NULL, 17, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('96394a43-eab6-4626-8b1f-040309f8ff0e', '内蒙古', 'NMG', '蒙', NULL, 12, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('a2630f50-39ed-4f27-bf0c-66c1d3f27df7', '广西省', 'GX', '桂', NULL, 21, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('c06e1be0-e0fb-40bf-b39c-9cf01d96007d', '甘肃省', 'GS', '陇', NULL, 28, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('d0722fc6-feb8-45ba-9d1c-f30871f67063', '安徽省', 'AH', '皖', NULL, 15, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('ddd88468-4142-4189-a6c6-f3dfd00515d0', '重庆市', 'CQ', '渝', NULL, 1, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('e0e5fdc2-1cc8-4743-84d1-2f3f5a88ad3e', '湖北省', 'HUB', '鄂', NULL, 18, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('e38eaf87-f8ec-41f1-9540-00d6274220b8', '广东省', 'GD', '粤', NULL, 20, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('e4da2d14-50b9-4520-9439-c9c8cf1de7e3', '四川省', 'SC', '蜀', NULL, 2, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('ebff77a5-f7d9-481e-8285-b1bad1af16ee', '黑龙江', 'HLJ', '黑', NULL, 11, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('ed981e82-0939-4732-b6d7-7f586b73a276', '钓鱼岛', 'DYD', '钓鱼岛', NULL, 34, 'a292d372-502e-46bc-9faa-077890ee33a7',
        1, 'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('f325b6b1-424e-4a4f-801f-7029191b47ee', '江苏省', 'JS', '苏', NULL, 13, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
INSERT INTO `L_STATE`
VALUES ('f819d7b2-0e42-4500-a124-2ce0a2d6e19e', '北京市', 'BJ', '京', NULL, 3, 'a292d372-502e-46bc-9faa-077890ee33a7', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:17', NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
