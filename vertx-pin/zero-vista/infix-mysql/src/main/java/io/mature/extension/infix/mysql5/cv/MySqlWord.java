package io.mature.extension.infix.mysql5.cv;

/**
 * MySql关键字
 */
public interface MySqlWord {
    interface Foreign {
        /* 设置NULL **/
        String SET_NULL = "SET NULL";

        /* 设置NO ACTION **/
        String NO_ACTION = "NO ACTION";

        /* 设置CASCADE */
        String CASCADE = "CASCADE";
    }

    interface Type {
        /* 0.TINYINT */
        String TINYINT = "TINYINT";
        /* 1.SMALLINT */
        String SMALLINT = "SMALLINT";
        /* 2.INT */
        String INT = "INT";
        /* 3.BIGINT */
        String BIGINT = "BIGINT";
        /* 4.DECIMAL */
        String DECIAML = "DECIMAL";
        /* 5.NUMBERIC */
        String NUMERIC = "NUMERIC";
        /* 6.FLOAT */
        String FLOAT = "FLOAT";
        /* 7.CHAR */
        String CHAR = "CHAR";
        /* 8.VARCHAR */
        String VARCHAR = "VARCHAR";
        /* 9.TEXT */
        String TEXT = "TEXT";
        /* 10.BIT */
        String BIT = "BIT";
        /* 11.BINARY */
        String BINARY = "BINARY";
        /* 12.VARBINARY */
        String VARBINARY = "VARBINARY";
        /* 13.DATE */
        String DATE = "DATE";
        /* 14.DATETIME */
        String DATETIME = "DATETIME";
        /* 15.TIME */
        String TIME = "TIME";
        /* 16.TIMESTAMP */
        String TIMESTAMP = "TIMESTAMP";
        /* 17.JSON */
        String JSON = "JSON";
    }

    interface Pattern {
        /* P_S */
        String P_S = "({0},{1})";
        /* P_N */
        String P_N = "({0})";
        /* 自增长 */
        String P_IDENTIFY = "AUTO_INCREMENT";
        /* Decimal */
        String P_DECIMAL = Type.DECIAML + P_S;
        /* Numeric */
        String P_NUMERIC = Type.NUMERIC + P_S;
        /* CHAR,VARCHAR */
        String P_CHAR = Type.CHAR + P_N;
        String P_VARCHAR = Type.VARCHAR + P_N;
        /* BINARY,VARBINARY */
        String P_BINARY = Type.BINARY + P_N;
        String P_VARBINARY = Type.VARBINARY + P_N;
    }

    interface Metadata {
        /* 数据库名 */
        String DATABASE = "TABLE_CATALOG";
        /* 表名 */
        String TABLE = "TABLE_NAME";
        /* 列名 */
        String COLUMN = "COLUMN_NAME";
        /* 约束名 */
        String CONSTRAINT_NAME = "CONSTRAINT_NAME";
        /* 约束类型 */
        String CONSTRAINT_TYPE = "CONSTRAINT_TYPE";

        String DATA_TYPE = "DATA_TYPE";
        String CHARACTER_LENGTH = "CHARACTER_MAXIMUM_LENGTH";
        String NUMERIC_PRECISION = "NUMERIC_PRECISION";
        String NUMERIC_SCALE = "NUMERIC_SCALE";
    }
}
