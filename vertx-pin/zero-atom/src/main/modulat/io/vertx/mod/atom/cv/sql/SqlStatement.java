package io.vertx.mod.atom.cv.sql;

/**
 * 标准Sql语句
 */
public interface SqlStatement {
    /*  表创建 */
    String TB_CREATE = "CREATE TABLE {0} ( {1} );";
    /*  表删除 */
    String TB_DROP = "DROP TABLE {0};";
    /*  表重命名 */
    String TB_RENAME = "ALTER TABLE {0} RENAME TO {1};";

    /*  视图创建语句 **/
    String V_CREATE = "CREATE VIEW {0} AS {1};";
    /*  视图删除语句 **/
    String V_DROP = "DROP VIEW {0};";

    /*  表约束更新语句 **/
    String ATBD_CONSTRAINT = "ALTER TABLE {0} DROP CONSTRAINT {1};";
    /* 表约束添加语句 */
    String ATBA_CONSTRAINT = "ALTER TABLE {0} ADD {1};";
    /* 表列添加语句 */
    String ATBA_COLUMN = "ALTER TABLE {0} ADD {1};";
    /* 表列重全名语句  oracle style old to new*/
    String ATBR_COLUMN = "ALTER TABLE {0} RENAME COLUMN {1} TO {2};";
    /* 表列删除语句 */
    String ATBD_COLUMN = "ALTER TABLE {0} DROP COLUMN {1};";
    /* 表列更新语句 */
    String ATBM_COLUMN = "ALTER TABLE {0} ALTER COLUMN {1};";

    /* 表统计 */
    String OP_COUNT = "SELECT COUNT(*) FROM {0}";
    /* 聚集函数通用 */
    String OP_AGGREGATE = "SELECT {0}({1}) FROM {2}";
    /* 插入语句 */
    String OP_INSERT = "INSERT INTO {0} ({1}) VALUES ({2})";
    /* 查询语句 */
    String OP_SELECT = "SELECT {0} FROM {1}";
    /* 更新语句 */
    String OP_UPDATE = "UPDATE {0} SET {1}";
    /* 删除语句 */
    String OP_DELETE = "DELETE FROM {0}";
    /* 调整Order专用语句 */
    String OP_ORDER = "UPDATE {0} SET I_ORDER = I_ORDER + 1 WHERE I_ORDER >= {1}";
    /* WHERE子句，后边部分可以依靠Expression实现 */
    String OP_WHERE = "WHERE {0}";
    /* 赋值语句，在Update中常用 */
    String OP_ASSIGN = "{0} = {1}";
    /* 排序语句 */
    String OP_ORDER_BY = "ORDER BY";

    /* 空值检测语句 */
    String SCHEMA_NULL = "SELECT COUNT(*) FROM {0} WHERE {1} __ NULL";
    /* Unique值检测语句 */
    String SCHEMA_UNIQUE = "SELECT COUNT(DISTINCT {1}) FROM {0} WHERE {1} IN (SELECT {1} FROM {0} GROUP BY {1} HAVING COUNT({1}) > 1)";

    /* Unique Key约束语句 */
    String CONSTRAINT_UK = "CONSTRAINT {0} UNIQUE ({1})";
    /* Primary Key约束语句 */
    String CONSTRAINT_PK = "CONSTRAINT {0} PRIMARY KEY ({1})";
    /* Foreign Key约束语句 */
    String CONSTRAING_FK = "CONSTRAINT {0} FOREIGN KEY ({1}) REFERENCES {2}({3})";
}
