package io.mature.extension.infix.oracle12.cv;

/**
 * MySql关键字
 */
public interface OracleWord {
    interface Foreign {
        /* 设置NULL **/
        String SET_NULL = "SET NULL";

        /* 设置NO ACTION **/
        String NO_ACTION = "NO ACTION";

        /* 设置CASCADE */
        String CASCADE = "CASCADE";
    }

    interface Type {
        // INIT Server数据类型
        // 整数==================================
        /** 0.TINYINT **/
        //String TINYINT = "TINYINT";
        /** 1.SMALLINT **/
        //String SMALLINT = "SMALLINT";
        /** 2.INT **/
        //String INT = "INT";
        /** 3.BIGINT **/
        //String BIGINT = "BIGINT";

        // 实数 =================================
        /**
         * 4.DECIMAL
         **/
        String DECIMAL = "FLOAT";
        /**
         * 5.NUMERIC
         **/
        String NUMERIC = "NUMBER";
        /** 6.SMALLMONEY **/
        //String SMALLMONEY = "SMALLMONEY";
        /** 7.MONEY **/
        //String MONEY = "MONEY";
        /** 8.REAL **/
        //String REAL = "REAL";
        /** 9.FLOAT **/
        //String FLOAT = "FLOAT";

        // 字符串================================
        /**
         * 10.CHAR
         **/
        String CHAR = "CHAR";
        /**
         * 11.NCHAR
         **/
        String NCHAR = "NCHAR";
        /**
         * 12.VARCHAR
         **/
        String VARCHAR = "VARCHAR2";
        /**
         * 13.NVARCHAR
         **/
        String NVARCHAR = "NVARCHAR2";
        /** 14.TEXT **/
        //String TEXT = "TEXT";
        /** 15.NTEXT **/
        //String NTEXT = "NTEXT";
        /** 16.VARCHAR(MAX) **/
        //String VARCHAR_MAX = "VARCHAR(MAX)";
        /** 17.NVARCHAR(MAX) **/
        //String NVARCHAR_MAX = "NVARCHAR(MAX)";

        // 二进制类型============================
        /** 18.BIT **/
        //String BIT = "BIT";
        /**
         * 19.BINARY
         **/
        String BINARY = "RAW";
        /** 20.VARBINARY **/
        //String VARBINARY = "VARBINARY";
        /** 21.VARBINARY(MAX) **/
        //String VARBINARY_MAX = "VARBINARY(MAX)";
        /** 22.IMAGE **/
        //String IMAGE = "IMAGE";

        // 时间类型==============================
        /** 23.SMALLDATETIME **/
        //String SMALLDATETIME = "SMALLDATETIME";
        /**
         * 24.DATE
         **/
        String DATE = "DATE";
        /** 25.DATETIME **/
        //String DATETIME = "DATETIME";
        /** 26.DATETIME2 **/
        //String DATETIME2 = "DATETIME2";
        /** 27.DATETIMEOFFSET **/
        //String DATETIMEOFFSET = "DATETIMEOFFSET";
        /** 28.TIME **/
        //String TIME = "TIME";
        /**
         * 29.TIMESTAMP
         **/
        String TIMESTAMP = "TIMESTAMP";

        // 其他类型==============================
        /** 30.XML **/
        //String XML = "XML";
        /** 31.GEOGRAPHY **/
        //String GEOGRAPHY = "GEOGRAPHY";
        /** 32.GEOMETRY **/
        //String GEOMETRY = "GEOMETRY";
        /** 33.HIERARCHYID **/
        //String HIERARCHYID = "HIERARCHYID";
        /** 34.UNIQUEIDENTIFIER **/
        //String UNIQUEIDENTIFIER = "UNIQUEIDENTIFIER";
    }

    interface Pattern { // 带格式的类型信息
        /**
         * 模式匹配中使用
         **/
        String P_S = "({0},{1})";
        /**
         * 模式匹配中使用
         **/
        String N = "({0})";
        /**
         * 模式匹配中使用
         **/
        String P = "({0})";
        /** 0.SQL Server中的自增长字段 **/
        //String P_IDENTIFY = "IDENTITY" + P_S;
        /**
         * 1.精度数据字段 DECIMAL
         **/
        String P_DECIMAL = Type.DECIMAL + P;
        /**
         * 2.精度数据字段 NUMERIC
         **/
        String P_NUMERIC = Type.NUMERIC + P_S;
        /**
         * 2.只有长度的数据字段 NUMERIC
         **/
        String P_NUMERICLEN = Type.NUMERIC + N;
        /**
         * 3.带长度CHAR
         **/
        String P_CHAR = Type.CHAR + N;
        /**
         * 4.带长度NCHAR
         **/
        String P_NCHAR = Type.NCHAR + N;
        /**
         * 5.带长度VARCHAR
         **/
        String P_VARCHAR = Type.VARCHAR + N;
        /**
         * 6.带长度NVARCHAR
         **/
        String P_NVARCHAR = Type.NVARCHAR + N;
        /**
         * 7.带长度的BINARY
         **/
        String P_BINARY = Type.BINARY + N;
        /** 8.带长度的DATETIME2 **/
        //String P_DATETIME2 = Type.DATETIME2 + N;
        /** 9.带长度的DATETIMEOFFSET **/
        //String P_DATETIMEOFFSET = Type.DATETIMEOFFSET + N;
    }

    interface Metadata {
        /* 数据库名 */
        String DATABASE = "OWNER";
        /* 表名 */
        String TABLE = "TABLE_NAME";
        /* 列名 */
        String COLUMN = "COLUMN_NAME";
        /* 约束名 */
        String CONSTRAINT_NAME = "CONSTRAINT_NAME";
        /* 约束类型 */
        String CONSTRAINT_TYPE = "CONSTRAINT_TYPE";
        /* 字段 nullable 信息 */
        String NULLABLE = "NULLABLE";
    }
}
