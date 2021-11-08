-- liquibase formatted sql

-- changeset Lang:ox-tabular-1
-- 列表数据表专用
DROP TABLE IF EXISTS X_TABULAR;
CREATE TABLE IF NOT EXISTS X_TABULAR
(
    `KEY`           VARCHAR(36) COMMENT '「key」- 列表主键',
    `NAME`          VARCHAR(255) COMMENT '「name」- 列表名称',
    `CODE`          VARCHAR(255) COMMENT '「code」- 列表编号',
    `TYPE`          VARCHAR(255) COMMENT '「type」- 列表类型',
    `ICON`          VARCHAR(255) COMMENT '「icon」- 列表图标',
    `SORT`          INTEGER COMMENT '「sort」- 排序信息',
    `COMMENT`       TEXT COMMENT '「comment」- 备注信息',
    `APP_ID`        VARCHAR(255) COMMENT '「appId」- 关联的应用程序ID',

    `RUN_COMPONENT` TEXT COMMENT '「runComponent」- 执行组件',
    -- 特殊字段
    `ACTIVE`        BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`         VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`      TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`      VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`    DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`    VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`    DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`    VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-tabular-2
ALTER TABLE X_TABULAR
    ADD UNIQUE (`APP_ID`, `TYPE`, `CODE`) USING BTREE; -- 每一个应用内的 app - type - code 维持唯一
ALTER TABLE X_TABULAR
    ADD UNIQUE (`SIGMA`, `TYPE`, `CODE`) USING BTREE;

ALTER TABLE X_TABULAR
    ADD INDEX IDXM_X_TABULAR_APP_ID_TYPE_ACTIVE (`APP_ID`, `TYPE`, `ACTIVE`) USING BTREE;
ALTER TABLE X_TABULAR
    ADD INDEX IDXM_X_TABULAR_SIGMA_TYPE_ACTIVE (`SIGMA`, `TYPE`, `ACTIVE`) USING BTREE;