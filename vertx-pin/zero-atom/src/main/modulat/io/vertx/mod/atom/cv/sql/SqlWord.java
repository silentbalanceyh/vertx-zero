package io.vertx.mod.atom.cv.sql;

public interface SqlWord {
    /**
     * 聚集函数
     **/
    interface Aggregate {
        /**
         * 0.统计函数
         **/
        String COUNT = "COUNT";
        /**
         * 1.最大值
         **/
        String MAX = "MAX";
        /**
         * 2.最小值
         **/
        String MIN = "MIN";
        /**
         * 3.求和
         **/
        String SUM = "SUM";
        /**
         * 4.求平均值
         **/
        String AVG = "AVG";
    }

    /**
     * 对象关键字
     **/
    interface Object { // 对象关键字
        /**
         * 0.数据库关键字
         **/
        String DATABASE = "DATABSE";
        /**
         * 1.数据表关键字
         **/
        String TABLE = "TABLE";
        /**
         * 2.数据列关键字
         **/
        String COLUMN = "COLUMN";
        /**
         * 3.数据视图
         **/
        String VIEW = "VIEW";
        /**
         * 4.索引
         **/
        String INDEX = "INDEX";
        /**
         * 5.约束
         **/
        String CONSTRANT = "CONSTRAINT";
        /**
         * 6.触发器
         **/
        String TRIGGER = "TRIGGER";
        /**
         * 7.键关键字
         **/
        String KEY = "KEY";
        /**
         * 8.存储过程
         **/
        String PROCEDURE = "PROCEDURE";
    }

    /**
     * 操作关键字
     **/
    interface Operation { // 操作关键字
        // DDL =============================================
        /**
         * 0.创建对象
         **/
        String CREATE = "CREATE";
        /**
         * 1.添加关键字
         **/
        String ADD = "ADD";
        /**
         * 2.删除对象关键字
         **/
        String DROP = "DROP";
        /**
         * 3.更改关键字
         **/
        String ALTER = "ALTER";
        // DML =============================================
        /**
         * 1.插入关键字
         **/
        String INSERT = "INSERT";
        /**
         * 2.更新关键字
         **/
        String UPDATE = "UPDATE";
        /**
         * 3.删除关键字
         **/
        String DELETE = "DELETE";
        /**
         * 4.查询关键字
         **/
        String SELECT = "SELECT";
        /**
         * 5.赋值关键字
         **/
        String SET = "SET";
    }

    /**
     * 连接关键字
     **/
    interface Connector { // 连接关键字
        /**
         * 1.AND
         **/
        String AND = "AND";
        /**
         * 2.OR
         **/
        String OR = "OR";
        /**
         * 3.FROM
         **/
        String FROM = "FROM";
        /**
         * 4.WHERE
         **/
        String WHERE = "WHERE";
        /**
         * 5.AS
         **/
        String AS = "AS";
        /**
         * 6.__
         **/
        String IS = "IS";
        /**
         * 7.BY
         **/
        String BY = "BY";
        /**
         * 8.ON
         **/
        String ON = "ON";
        /**
         * 9.JOIN
         **/
        String JOIN = "JOIN";
        /**
         * 10.HAVING
         **/
        String HAVING = "HAVING";
    }

    /**
     * 比较操作符
     **/
    interface Comparator { // 比较操作符
        /**
         * 0.IN
         **/
        String IN = "IN";
        /**
         * 1.NOT
         **/
        String NOT = "NOT";
        /**
         * 2.NULL
         **/
        String NULL = "NULL";
        /**
         * 3.LIKE
         **/
        String LIKE = "LIKE";
        /**
         * 4.%
         */
        String PERCENT = "%";
        /**
         * 5.等于 =
         **/
        String EQ = "=";
        /**
         * 6.小于 <
         **/
        String LT = "<";
        /**
         * 7.大于 >
         **/
        String GT = ">";
        /**
         * 8.不等于 <>
         **/
        String NEQ = LT + GT;
        /**
         * 9.小于等于 <=
         **/
        String LET = LT + EQ;
        /**
         * 10.大于等于 >=
         **/
        String GET = GT + EQ;
    }

    /**
     * 辅助关键字
     **/
    interface Assistant { // 辅助关键字
        /**
         * 0.UNIQUE
         **/
        String UNIQUE = "UNIQUE";
        /**
         * 1.PRIMARY
         **/
        String PRIMARY = "PRIMARY";
        /**
         * 2.FOREIGN
         **/
        String FOREIGN = "FOREIGN";
        /**
         * 3.REFERENCES
         **/
        String REFERENCES = "REFERENCES";
        /**
         * 4.ORDER
         **/
        String ORDER = "ORDER";
        /**
         * 5.GROUP
         **/
        String GROUP = "GROUP";
        /**
         * 6.ASC
         **/
        String ASC = "ASC";
        /**
         * 7.DESC
         **/
        String DESC = "DESC";
        /**
         * 8.DISTINCT 去重
         **/
        String DISTINCT = "DISTINCT";
        /**
         * 9.TOP
         **/
        String TOP = "TOP";
        /**
         * 10.COMMENT
         **/
        String COMMENT = "COMMENT";
    }
}
