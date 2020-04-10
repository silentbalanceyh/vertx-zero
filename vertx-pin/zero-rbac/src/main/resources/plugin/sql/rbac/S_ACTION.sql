-- liquibase formatted sql

-- changeset Lang:ox-action-1
-- 操作专用表：S_ACTION
DROP TABLE IF EXISTS S_ACTION;
CREATE TABLE IF NOT EXISTS S_ACTION
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 操作ID',
    `NAME`           VARCHAR(255) COMMENT '「name」- 操作名称',
    `CODE`           VARCHAR(255) COMMENT '「code」- 操作码',
    `RESOURCE_ID`    VARCHAR(36) COMMENT '「resourceId」- 操作关联资源ID',
    `PERMISSION_ID`  VARCHAR(36) COMMENT '「permissionId」- 操作所属权限',
    `LEVEL`          INTEGER COMMENT '「level」- 操作级别, ACL控制',

    -- 操作行为捕捉
    `URI`            VARCHAR(255) COMMENT '「uri」- 资源地址',
    `METHOD`         VARCHAR(32) COMMENT '「method」- 资源方法',
    `SIGMA`          VARCHAR(32) COMMENT '「sigma」- 统一标识',

    /*
     * 刷新的凭证，刷新的凭证限制条件：
     * 1. 视图不可变更（只能同一个视图刷新）
     * 2. 只能刷新 User 的视图，因为如果是 Role 的视图，刷新自己的没有作用，需要发送通知让其他人重新登录
     * 3. 只能同一个 sigma 的资源视图被刷新
     * 4. 凭证为 JsonArray 格式，每一个元素都是
     *    -- METHOD URI
     */
    `RENEWAL_CREDIT` TEXT COMMENT '「renewalCredit」- 被刷新的凭证',

    -- 特殊字段
    `LANGUAGE`       VARCHAR(10) COMMENT '「language」- 使用的语言',
    `ACTIVE`         BIT COMMENT '「active」- 是否启用',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置数据',
    `COMMENT`        TEXT COMMENT '「action」- 操作说明',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);

-- changeset Lang:ox-action-2
-- Unique Key：独立唯一主键
ALTER TABLE S_ACTION
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE S_ACTION
    ADD UNIQUE (`RESOURCE_ID`); -- 操作和资源一对一绑定
ALTER TABLE S_ACTION
    ADD UNIQUE (`URI`, `METHOD`, `SIGMA`); -- 操作和资源一对一绑定