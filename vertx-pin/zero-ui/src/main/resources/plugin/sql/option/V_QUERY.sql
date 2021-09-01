-- liquibase formatted sql

-- changeset Lang:ox-option-query-1
-- 查询选项：V_QUERY
DROP TABLE IF EXISTS V_QUERY;
CREATE TABLE IF NOT EXISTS V_QUERY
(
    `KEY`        VARCHAR(36) COMMENT '「key」- 选项主键',
    `PROJECTION` TEXT COMMENT '「projection」- query/projection:[], 默认列过滤项',
    `PAGER`      TEXT COMMENT '「pager」- query/pager:{}, 分页选项',
    `SORTER`     TEXT COMMENT '「sorter」- query/sorter:[], 排序选项',
    `CRITERIA`   TEXT COMMENT '「criteria」- query/criteria:{}, 查询条件选项',
    PRIMARY KEY (`KEY`) USING BTREE
);