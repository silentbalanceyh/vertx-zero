package io.modello.dynamic.modular.sql;

import io.vertx.mod.atom.cv.sql.SqlStatement;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DML类型的 Sql 语句的创建器，提供给Dao使用
 */
public class SqlDMLBuilder implements SqlStatement {
    private static SqlDMLBuilder INSTANCE;

    private SqlDMLBuilder() {
    }

    public static SqlDMLBuilder create() {
        synchronized (SqlDMLBuilder.class) {
            if (null == INSTANCE) {
                INSTANCE = new SqlDMLBuilder();
            }
            return INSTANCE;
        }
    }

    /**
     * 构造 INSERT 语句
     * INSERT INTO TABLE (COLUMN1,COLUMN2,...) VALUES (?,?,...)
     */
    public String buildInsert(final String table,
                              final Collection<String> columns,
                              final char wrapperChar) {
        final int paramLen = columns.size();
        /* 顺序一致性 */
        final List<String> paramList = new ArrayList<>();
        Ut.itRepeat(paramLen, () -> paramList.add("?"));
        /* 列名处理 */
        final List<String> columnList = new ArrayList<>();
        columns.forEach(column -> columnList.add(wrapperChar + column + wrapperChar));
        /* 模板 */
        final String columnPart = Ut.fromJoin(columnList, ",");
        final String valuePart = Ut.fromJoin(paramList, ",");
        return MessageFormat.format(SqlStatement.OP_INSERT, table, columnPart, valuePart);
    }
}
