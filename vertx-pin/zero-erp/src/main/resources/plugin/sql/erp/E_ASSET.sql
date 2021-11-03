-- liquibase formatted sql

-- changeset Lang:e-asset-1
DROP TABLE IF EXISTS `E_ASSET`;
CREATE TABLE `E_ASSET`
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 资产主键',
    `NAME`            VARCHAR(255) COMMENT '「name」- 资产名称',
    `CODE`            VARCHAR(255) COMMENT '「code」- 资产编号',
    `TYPE`            VARCHAR(36) COMMENT '「type」- 资产类型',
    `STATUS`          VARCHAR(36) COMMENT '「status」- 资产状态',

    -- 商品属性
    `MODEL_NUMBER`    VARCHAR(255) COMMENT '「modelNumber」- 规格型号',
    `UNIT`            VARCHAR(64) COMMENT '「unit」- 计量单位',

    -- 数量信息
    `NUM`             BIGINT COMMENT '「num」- 资产数量',
    `NUM_DEPRECATING` BIGINT COMMENT '「numDeprecating」- 预计折旧数量',
    `NUM_DEPRECATED`  BIGINT COMMENT '「numDeprecated」- 已折旧数量',
    `NUM_USING`       BIGINT COMMENT '「numUsing」- 预计使用数量',
    `NUM_USED`        BIGINT COMMENT '「numUsed」- 已使用数量',

    -- 变动，折旧，使用
    `WAY_CHANGE`      VARCHAR(64) COMMENT '「wayChange」- 变动方式',
    `WAY_DEPRECATE`   VARCHAR(64) COMMENT '「wayDeprecate」- 折旧方式',
    `WAY_ACCORDING`   VARCHAR(64) COMMENT '「wayAccording」- 折旧依据',
    `USED_AT`         DATETIME COMMENT '「usedAt」- 开始使用时间',
    `USED_BY`         VARCHAR(64) COMMENT '「usedBy」- 使用者',
    `USED_STATUS`     VARCHAR(255) COMMENT '「usedStatus」- 使用状态',

    -- 价值属性
    `V_ORIGINAL`      DECIMAL(18, 2) COMMENT '「vOriginal」- 原价值',
    `V_TAX`           DECIMAL(18, 2) COMMENT '「vTax」- 税额',
    `V_DE_READY`      DECIMAL(18, 2) COMMENT '「vDeReady」- 减值准备',
    `V_NET_JUNK`      DECIMAL(18, 2) COMMENT '「vNetJunk」- 净残值',
    `V_NET`           DECIMAL(18, 2) COMMENT '「vNet」- 净值',
    `V_NET_AMOUNT`    DECIMAL(18, 2) COMMENT '「vNetAmount」- 净额',
    `V_DEPRECATED_M`  DECIMAL(18, 2) COMMENT '「vDeprecatedM」- 月折旧',
    `V_DEPRECATED_A`  DECIMAL(18, 2) COMMENT '「vDeprecatedA」- 累积折旧',

    -- 财务属性
    `K_FIXED`         VARCHAR(36) COMMENT '「kFixed」- 固定资产科目',
    `K_DEPRECATED`    VARCHAR(36) COMMENT '「kDeprecated」- 累积折旧科目',
    `K_ASSIGNMENT`    VARCHAR(36) COMMENT '「kAssignment」- 折旧费用分配科目',
    `K_TAX`           VARCHAR(36) COMMENT '「kTax」- 税金科目',
    `K_DEVALUE`       VARCHAR(36) COMMENT '「kDevalue」- 减值准备科目',
    `K_CHANGE`        VARCHAR(36) COMMENT '「kChange」- 资产变动对方科目',

    -- 供应商信息
    `CUSTOMER_ID`     VARCHAR(36) COMMENT '「customerId」- 供应商ID',
    `EXPIRED_AT`      DATETIME COMMENT '「expiredAt」- 到期时间',
    `EXPIRED_COMMENT` TEXT COMMENT '「expiredComment」- 到期说明',

    -- 关联属性
    `USER_ID`         VARCHAR(36) COMMENT '「userId」- 资产管理者',
    `STORE_ID`        VARCHAR(36) COMMENT '「storeId」- 所属仓库ID',
    `DEPT_ID`         VARCHAR(36) COMMENT '「deptId」- 所属部门',
    `COMPANY_ID`      VARCHAR(36) COMMENT '「companyId」- 所属公司',
    `PARENT_ID`       VARCHAR(36) COMMENT '「parentId」- 上级资产',
    `COMMENT`         TEXT COMMENT '「comment」- 资产备注',

    -- 特殊字段
    `SIGMA`           VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`        VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`          BIT COMMENT '「active」- 是否启用',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置数据',

    -- 时间处理
    `ENTER_AT`        DATETIME COMMENT '「enterAt」- 入库时间',
    `ENTER_BY`        VARCHAR(36) COMMENT '「enterBy」- 入库人',
    `ACCOUNT_AT`      DATETIME COMMENT '「accountAt」- 入账时间',
    `ACCOUNT_BY`      VARCHAR(36) COMMENT '「accountBy」- 入账人',
    `SCRAP_AT`        DATETIME COMMENT '「scrapAt」- 报废时间',
    `SCRAP_BY`        VARCHAR(36) COMMENT '「scrapBy」- 报废人',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:e-asset-2
ALTER TABLE E_ASSET
    ADD UNIQUE (`NAME`, `SIGMA`);
ALTER TABLE E_ASSET
    ADD UNIQUE (`CODE`, `SIGMA`);

