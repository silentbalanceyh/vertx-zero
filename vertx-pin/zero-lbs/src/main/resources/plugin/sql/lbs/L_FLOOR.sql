-- liquibase formatted sql

-- changeset Lang:l-floor-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for L_FLOOR
-- ----------------------------
DROP TABLE IF EXISTS `L_FLOOR`;
CREATE TABLE `L_FLOOR`
(
    `KEY`        VARCHAR(36) NOT NULL COMMENT '「key」- 主键',
    `NAME`       VARCHAR(32) NOT NULL COMMENT '「name」- 名称',
    `CODE`       VARCHAR(36) DEFAULT NULL COMMENT '「code」- 编码',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `ORDER`      INT(11)     DEFAULT NULL COMMENT '「order」- 排序',

    `TENT_ID`    varchar(36) DEFAULT NULL COMMENT '「tentId」- 栋ID',

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

-- changeset Lang:l-floor-2
ALTER TABLE L_FLOOR
    ADD UNIQUE (`CODE`, `SIGMA`);

-- changeset Lang:l-floor-3
-- ----------------------------
-- Records of L_FLOOR
-- ----------------------------

INSERT INTO `L_FLOOR`
VALUES ('49025f71-ad7f-4501-89a6-3e0356727e4f', '1栋-3楼', 'T1-F3', NULL, 3, 'd2c971a2-5c43-4baf-8692-c735419c1d13', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('4f1f32af-32a2-40c9-9a81-4ec683ac90ac', '3栋-1楼', 'T3-F1', NULL, 1, '1d9a21ca-7463-43e0-adc3-77076c2f4be6', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('71a28988-4424-4f90-9eef-dab5a2b90153', '3栋-2楼', 'T3-F2', NULL, 2, '1d9a21ca-7463-43e0-adc3-77076c2f4be6', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('8e8a80a5-c231-4f4d-adca-bbefc36fd85a', '2栋-2楼', 'T2-F2', NULL, 2, '6beec50f-ecca-4f9a-854e-e1db69f2d47f', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('a9bfccda-c8b4-4bdc-8482-bd4c70e10880', '1栋-2楼', 'T1-F2', NULL, 2, 'd2c971a2-5c43-4baf-8692-c735419c1d13', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('d2910177-d6c7-420f-b905-a00ee37f5d8a', '1栋-1楼', 'T1-F1', NULL, 1, 'd2c971a2-5c43-4baf-8692-c735419c1d13', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('d5773c93-a34f-4721-a949-aabf43e5138a', '2栋-1楼', 'T2-F1', NULL, 1, '6beec50f-ecca-4f9a-854e-e1db69f2d47f', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);
INSERT INTO `L_FLOOR`
VALUES ('ee44b9f5-777a-4504-8c27-f214548da11e', '2栋-3楼', 'T2-F3', NULL, 3, '6beec50f-ecca-4f9a-854e-e1db69f2d47f', 1,
        'ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq', 'cn', NULL, '2019-06-17 11:04:25', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
