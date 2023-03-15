-- liquibase formatted sql

-- changeset Lang:b-bag-1
/*
 * 应用模块表：B_BAG
 * 用来描述一个应用中启用的模块信息（包基础信息）
 * 1. 用户登录到应用过后可以进入应用的开发中心
 * 2. 根据 License 算法启用开发中心现存的包
 * 3. 模块和模块之间会有依赖关系，存储在 B_BELT 中（描述模块和模块之间的依存关系）
 * 目前可标准化的模块包：
 * 业务包
 * -- CMDB                  （配置化管理），银行可用
 * -- ASSET                 （资产管理），银行可用
 * -- ISO27000              （督办、审计、连续性管理），银行可用
 * -- IOT                   （物联网包）
 * -- HBM                   （HBM管理包）
 * -- ZERO-F                （基础层，技术性内容）
 * -- ZERO-E                （业务层，业务性内容）
 */
DROP TABLE IF EXISTS B_BAG;
CREATE TABLE IF NOT EXISTS B_BAG
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 模块主键',
    -- 基础信息
    `NAME`       VARCHAR(255) COMMENT '「name」- 模块名称',
    `NAME_ABBR`  VARCHAR(255) COMMENT '「nameAbbr」- 模块缩写',
    `NAME_FULL`  VARCHAR(255) COMMENT '「nameFull」- 模块全名',
    /*
     *  Z-KERNEL：            内核模块
     *  Z-FOUNDATION：        基础模块
     *  Z-COMMERCE：          业务模块
     *  SPECIFICATION：       标准化模块
     *  EXTENSION：           甲方专用模块
     */
    `TYPE`       VARCHAR(64) COMMENT '「type」- 包类型',

    `UI_ICON`    VARCHAR(255) COMMENT '「uiIcon」- 模块图标',
    `UI_STYLE`   TEXT COMMENT '「uiStyle」- 模块风格',
    `UI_SORT`    BIGINT COMMENT '「uiSort」- 模块排序',

    -- 模块核心配置
    `UI_CONFIG`  LONGTEXT COMMENT '「uiConfig」- 模块核心配置',

    `ENTRY`      BIT         DEFAULT NULL COMMENT '「entry」- 是否入口（带入口为应用，当前APP_ID下安装内容）',
    `ENTRY_ID`   VARCHAR(36) DEFAULT NULL COMMENT '「entryId」- 入口专用ID，关联 X_MENU 中的ID，其余的直接使用链接',
    -- 应用ID
    `APP_ID`     VARCHAR(36) COMMENT '「appId」- 关联的应用程序ID',
    `PARENT_ID`  VARCHAR(36) COMMENT '「parentId」- 父包ID',

    -- 特殊字段
    `ACTIVE`     BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`      VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`   VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:b-bag-2
ALTER TABLE B_BAG
    ADD UNIQUE (`NAME`, `APP_ID`);
ALTER TABLE B_BAG
    ADD UNIQUE (`NAME_ABBR`, `APP_ID`);
