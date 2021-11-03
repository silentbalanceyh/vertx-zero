-- liquibase formatted sql

-- changeset Lang:p-commodity-1
-- 产品表：P_COMMODITY
DROP TABLE IF EXISTS P_COMMODITY;
CREATE TABLE IF NOT EXISTS P_COMMODITY
(
    `KEY`          VARCHAR(36) COMMENT '「key」- 产品主键',
    `NAME`         VARCHAR(255) COMMENT '「name」- 产品名称',
    `CODE`         VARCHAR(255) COMMENT '「code」- 产品编号（系统可用）',
    `TYPE`         VARCHAR(36) COMMENT '「type」- 产品类型',
    `STATUS`       VARCHAR(36) COMMENT '「status」- 产品状态',
    -- 商品信息
    `TAGS`         TEXT COMMENT '「tags」- 商品标签',
    `ORIGIN`       TEXT COMMENT '「origin」- 商品产地',
    `BAR_CODE`     TEXT COMMENT '「barCode」- 条形码',
    `HELP_CODE`    VARCHAR(255) COMMENT '「helpCode」- 助记码',
    `BRAND_ID`     VARCHAR(36) COMMENT '「brandId」- 品牌',
    `MODEL_NUMBER` VARCHAR(255) COMMENT '「modelNumber」- 规格型号',
    `UNIT`         VARCHAR(64) COMMENT '「unit」- 计量单位',
    `EXPIRED_DAY`  INTEGER COMMENT '「expiredDay」- 保质天数',

    -- 财务信息
    `K_WAY_COST`   VARCHAR(36) COMMENT '「kWayCost」- 成本计算方法',
    `K_BY_BATCH`   BIT         DEFAULT NULL COMMENT '「kByBatch」- 按批核算成本',
    `K_TAX_TYPE`   VARCHAR(36) COMMENT '「kTaxType」- 税收分类编码',
    `K_TAX_RATE`   DECIMAL(10, 2) COMMENT '「kTaxRate」- 税率',

    -- 默认信息
    `DF_CUSTOMER`  VARCHAR(36) COMMENT '「dfCustomer」- 默认供应商',
    `DF_WH`        VARCHAR(36) COMMENT '「dfWh」- 默认仓库',

    -- 加权平均结果（需计算）
    `LOGO`         VARCHAR(255) COMMENT '「logo」- 产品图片',
    `PRICE`        DECIMAL(18, 2) NOT NULL COMMENT '「price」- 商品单价',
    `QUANTITY`     INT            NOT NULL COMMENT '「quantity」- 商品数量',
    `AMOUNT`       DECIMAL(18, 2) NOT NULL COMMENT '「amount」——总价，理论计算结果',
    `COMMENT`      TEXT COMMENT '「comment」- 产品备注',

    -- 特殊字段
    `ACTIVE`       BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`        VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`     TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`     VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`   DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`   VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`   DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`   VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:p-commodity-2
ALTER TABLE P_COMMODITY
    ADD UNIQUE (`CODE`, `SIGMA`); -- 商品编号不重复