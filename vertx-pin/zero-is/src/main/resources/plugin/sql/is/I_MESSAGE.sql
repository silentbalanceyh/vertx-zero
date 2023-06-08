-- liquibase formatted sql

-- changeset Lang:i-message-1
-- 消息队列：I_MESSAGE
DROP TABLE IF EXISTS I_MESSAGE;
CREATE TABLE IF NOT EXISTS I_MESSAGE
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 消息主键',
    `NAME`       VARCHAR(255) COMMENT '「name」- 消息名称',
    `CODE`       VARCHAR(36) COMMENT '「code」- 消息编码',

    `TYPE`       VARCHAR(255) COMMENT '「type」- 消息类型',
    /*
     * P2P, Point 2 Point
     * -- status:
     *    PENDING -> SENT ( FAILED )
     * 1) Email, Sms etc. ( Reflect to field type )
     * 2) Capture the tpl based on `type` at the same time
     */
    `STATUS`     VARCHAR(255) COMMENT '「status」- 消息状态',
    `SUBJECT`    VARCHAR(255) COMMENT '「subject」- 消息标题',
    `CONTENT`    LONGTEXT COMMENT '「content」- 消息内容',

    `FROM`       VARCHAR(255) COMMENT '「from」- 消息发送方',
    `TO`         VARCHAR(255) COMMENT '「to」- 消息接收方',

    `SEND_BY`    VARCHAR(36) COMMENT '「sendBy」- 发送者',
    `SEND_AT`    VARCHAR(36) COMMENT '「sendAt」- 发送时间',

    -- 特殊属性
    `APP_ID`     VARCHAR(36) COMMENT '「appId」- 所属应用ID',

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

-- changeset Lang:i-message-2
ALTER TABLE I_MESSAGE
    ADD UNIQUE (`APP_ID`, `CODE`); -- 模板名称/编码
ALTER TABLE I_MESSAGE
    ADD UNIQUE (`APP_ID`, `NAME`);