-- liquibase formatted sql

-- changeset Lang:f-invoice-1
DROP TABLE IF EXISTS `F_INVOICE`;
CREATE TABLE `F_INVOICE`
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 发票主键',
    `NAME`           VARCHAR(255)   NOT NULL COMMENT '「name」- 发票名称',
    `CODE`           VARCHAR(255)   NOT NULL COMMENT '「code」- 发票机器码',

    -- 维度信息
    `TYPE`           VARCHAR(36)    NOT NULL COMMENT '「type」- 发票类型',

    -- 基本信息
    `AMOUNT`         DECIMAL(18, 2) NOT NULL COMMENT '「amount」- 发票金额',
    `COMMENT`        LONGTEXT COMMENT '「comment」 - 发票备注',
    -- 发票信息
    `INVOICE_TITLE`  VARCHAR(255)   NOT NULL COMMENT '「invoiceTitle」- 发票抬头',
    `INVOICE_NUMBER` VARCHAR(255)   NOT NULL COMMENT '「invoiceNumber」- 发票代码',
    `INVOICE_SERIAL` VARCHAR(255)   NOT NULL COMMENT '「invoiceSerial」- 发票号码',
    -- 打印信息
    `TIN`            VARCHAR(255) COMMENT '「tin」- 税号：纳税人识别号',
    `TIN_NAME`       VARCHAR(255) COMMENT '「tinName」- 纳税人姓名',
    `PERSONAL`       BIT COMMENT '「personal」- 是否个人发票',
    `DESC_BANK`      TEXT COMMENT '「descBank」- 开户行信息',
    `DESC_COMPANY`   TEXT COMMENT '「descCompany」- 公司信息',
    `DESC_LOCATION`  TEXT COMMENT '「descLocation」- 地址电话',
    `DESC_USER`      TEXT COMMENT '「descUser」- 个人发票用户信息',
    -- 开票人
    `NAME_RECEIPT`   VARCHAR(36) COMMENT '「nameReceipt」收款人',
    `NAME_RECHECK`   VARCHAR(36) COMMENT '「nameRecheck」复核人',
    `NAME_BILLING`   VARCHAR(36) COMMENT '「nameBilling」开票人',
    `NAME_SELLING`   VARCHAR(36) COMMENT '「nameSelling」销售人',

    -- 打印信息
    `ORDER_ID`       VARCHAR(36) COMMENT '「orderId」- 订单对应的订单ID',

    -- 特殊字段
    `SIGMA`          VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`       VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`         BIT COMMENT '「active」- 是否启用',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:f-invoice-2
ALTER TABLE F_INVOICE
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE F_INVOICE
    ADD UNIQUE (`INVOICE_NUMBER`, `SIGMA`);
ALTER TABLE F_INVOICE
    ADD UNIQUE (`INVOICE_SERIAL`, `SIGMA`);
ALTER TABLE F_INVOICE
    ADD INDEX IDX_F_INVOICE_ORDER_ID (`ORDER_ID`);