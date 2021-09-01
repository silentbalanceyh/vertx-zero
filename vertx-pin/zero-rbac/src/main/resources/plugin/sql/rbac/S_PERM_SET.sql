-- liquibase formatted sql

-- changeset Lang:ox-perm-set-1
-- 权限专用表：S_PERMISSION
DROP TABLE IF EXISTS S_PERM_SET;
CREATE TABLE IF NOT EXISTS S_PERM_SET
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 权限集ID',
    `NAME`       VARCHAR(255) COMMENT '「name」- 权限集名称',
    `CODE`       VARCHAR(255) COMMENT '「code」- 权限集关联权限代码',

    -- 权限基础信息
    -- 三层维度处理
    -- 1）type，基本权限类型：系统权限，开发权限，业务权限
    -- 2）name，权限集
    -- 3）三级，直接在name中支持：<level1>/<level2>/<level3> 实现分类排序（约定，非必须）
    `TYPE`       VARCHAR(255) COMMENT '「type」- 权限集类型',

    -- 特殊字段
    `SIGMA`      VARCHAR(128) COMMENT '「sigma」- 绑定的统一标识',
    `LANGUAGE`   VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`     BIT COMMENT '「active」- 是否启用',
    `COMMENT`    TEXT COMMENT '「comment」- 权限集说明',
    `METADATA`   TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT` DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY` VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT` DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY` VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-perm-set-2
-- Unique Key：独立唯一键定义
-- 关于权限集说明
-- 1）同一个权限集中包含的权限不可重复
-- 2）资源定义好过后，权限集在新版本中作为过滤菜单处理
-- 3）管理界面的第一部分直接使用权限集和权限关联关系执行
ALTER TABLE S_PERM_SET
    ADD UNIQUE (`NAME`, `CODE`, `SIGMA`) USING BTREE;