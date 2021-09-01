-- liquibase formatted sql

-- changeset Lang:ox-attachment-1
DROP TABLE IF EXISTS X_ATTACHMENT;
CREATE TABLE IF NOT EXISTS X_ATTACHMENT
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 附件的ID值',
    `STORE_WAY`  VARCHAR(12) COMMENT '「storeWay」- 存储方式，BLOB / FILE / TPL / REMOTE',
    `STATUS`     VARCHAR(12) COMMENT '「status」- 状态，PROGRESS / SUCCESS',
    `NAME`       VARCHAR(255) COMMENT '「name」- 文件名（带扩展名）',
    `FILE_NAME`  VARCHAR(255) COMMENT '「fileName」- 原始文件名（不带扩展名）',
    `FILE_KEY`   VARCHAR(255) COMMENT '「fileKey」- TPL模式中的文件唯一的key（全局唯一）',
    `FILE_URL`   VARCHAR(255) COMMENT '「fileUrl」- 该文件的下载链接（全局唯一）',
    `FILE_PATH`  VARCHAR(255) COMMENT '「filePath」- 该文件的存储地址，FILE时使用',
    `EXTENSION`  VARCHAR(10) COMMENT '「extension」- 文件扩展名',
    `MODULE`     VARCHAR(64) COMMENT '「module」- 业务标识',
    `MIME`       VARCHAR(64) COMMENT '「mime」- 该文件的MIME类型',
    `SIZE`       INTEGER COMMENT '「size」- 该文件的尺寸',

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
    PRIMARY KEY (`KEY`) USING BTREE
);

-- changeset Lang:ox-attachment-2
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_KEY`) USING BTREE;
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_URL`) USING BTREE;
ALTER TABLE X_ATTACHMENT
    ADD UNIQUE (`FILE_PATH`) USING BTREE;