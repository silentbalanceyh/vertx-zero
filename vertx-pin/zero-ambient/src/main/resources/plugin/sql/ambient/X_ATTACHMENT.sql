-- liquibase formatted sql

-- changeset Lang:ox-attachment-1
DROP TABLE IF EXISTS X_ATTACHMENT;
CREATE TABLE IF NOT EXISTS X_ATTACHMENT
(
    `KEY`            VARCHAR(36) COMMENT '「key」- 附件的ID值',
    `NAME`           VARCHAR(255) COMMENT '「name」- 文件名（带扩展名）',
    `EXTENSION`      VARCHAR(10) COMMENT '「extension」- 文件扩展名',
    -- 文件管理专用
    `TYPE`           VARCHAR(128) COMMENT '「type」- 文件类型，直接关联zero.file.tree类型',

    `MIME`           VARCHAR(128) COMMENT '「mime」- 该文件的MIME类型',
    `SIZE`           INTEGER COMMENT '「size」- 该文件的尺寸',

    `STATUS`         VARCHAR(12) COMMENT '「status」- 状态，PROGRESS / SUCCESS',
    `STORE_WAY`      VARCHAR(12) COMMENT '「storeWay」- 存储方式，BLOB / FILE / TPL / REMOTE',
    `STORE_ID`       VARCHAR(36) COMMENT '「storeId」- 存储关联的Integration ID',

    -- 关联模型信息
    `MODEL_ID`       VARCHAR(255) COMMENT '「modelId」- 关联的模型identifier，用于描述',
    `MODEL_KEY`      VARCHAR(36) COMMENT '「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录',
    `MODEL_CATEGORY` VARCHAR(36) COMMENT '「modelCategory」- 如果一个模型记录包含多种附件，则需要设置模型相关字段，等价于 field',

    -- 原始文件相关信息
    `FILE_NAME`      VARCHAR(255) COMMENT '「fileName」- 原始文件名（不带扩展名）',
    `FILE_KEY`       VARCHAR(255) COMMENT '「fileKey」- TPL模式中的文件唯一的key（全局唯一）',
    `FILE_URL`       VARCHAR(255) COMMENT '「fileUrl」- 该文件的下载链接（全局唯一）',
    `FILE_PATH`      VARCHAR(255) COMMENT '「filePath」- 该文件的存储地址，FILE时使用',

    -- 特殊字段
    `ACTIVE`         BIT         DEFAULT NULL COMMENT '「active」- 是否启用',
    `SIGMA`          VARCHAR(32) DEFAULT NULL COMMENT '「sigma」- 统一标识',
    `METADATA`       TEXT COMMENT '「metadata」- 附加配置',
    `LANGUAGE`       VARCHAR(8)  DEFAULT NULL COMMENT '「language」- 使用的语言',

    -- Auditor字段
    `CREATED_AT`     DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`     VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`     DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`     VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-attachment-2
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_KEY`) USING BTREE;
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_URL`) USING BTREE;
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_PATH`) USING BTREE;