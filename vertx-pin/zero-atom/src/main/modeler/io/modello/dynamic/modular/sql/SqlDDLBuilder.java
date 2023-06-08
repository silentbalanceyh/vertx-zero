package io.modello.dynamic.modular.sql;

import io.horizon.eon.VString;
import io.vertx.mod.atom.cv.sql.SqlStatement;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.List;

/**
 * Sql DDL 语句构造器
 */
public final class SqlDDLBuilder implements SqlStatement {

    private static SqlDDLBuilder INSTANCE;

    private SqlDDLBuilder() {
    }

    public static SqlDDLBuilder create() {
        synchronized (SqlDDLBuilder.class) {
            if (null == INSTANCE) {
                INSTANCE = new SqlDDLBuilder();
            }
            return INSTANCE;
        }
    }

    /* 表创建 */
    public String buildCreateTable(final String tableName,
                                   final List<String> lines) {
        return MessageFormat.format(SqlStatement.TB_CREATE, tableName,
            Ut.fromJoin(lines, VString.COMMA));
    }

    /* 表删除 */
    public String buildDropTable(final String table) {
        return MessageFormat.format(SqlStatement.TB_DROP, table);
    }

    /* 表重命名 */
    public String buildRenameTable(final String table) {
        return MessageFormat.format(SqlStatement.TB_RENAME, table, SqlDDLConstant.combineNewName(table));
    }

    /* 表更新：删除列 */
    public String buildDropColumn(final String table,
                                  final String column) {
        return MessageFormat.format(SqlStatement.ATBD_COLUMN, table, column);
    }

    /* 表更新：假删除列重命名 */
    public String buildDropRenameColumn(final String table,
                                        final String column, final String newColumn) {
        return MessageFormat.format(SqlStatement.ATBR_COLUMN, table, column, newColumn);
    }

    /* 表更新：更新列 */
    public String buildAlterColumn(final String table,
                                   final String colLine) {
        return MessageFormat.format(SqlStatement.ATBM_COLUMN, table, colLine);
    }

    /* 表更新：添加列 */
    public String buildAddColumn(final String table,
                                 final String colLine) {
        return MessageFormat.format(SqlStatement.ATBA_COLUMN, table, colLine);
    }

    /* 表更新：添加约束 */
    public String buildAddConstraint(final String table,
                                     final String csLine) {
        return MessageFormat.format(SqlStatement.ATBA_CONSTRAINT, table, csLine);
    }

    /* 表更新：删除约束 */
    public String buildDropConstraint(final String table,
                                      final String constraint) {
        return MessageFormat.format(SqlStatement.ATBD_CONSTRAINT, table, constraint);
    }

    /* 检查指定列是否有空数据 */
    public String buildNullSQL(final String table,
                               final String column) {
        return MessageFormat.format(SqlStatement.SCHEMA_NULL, table, column);
    }

    /* 检查指定列是否有重复数据 */
    public String buildUniqueSQL(final String table,
                                 final String column) {
        return MessageFormat.format(SqlStatement.SCHEMA_UNIQUE, table, column);
    }

    /* 检查表中数据量 */
    public String buildRowsSQL(final String table) {
        return MessageFormat.format(SqlStatement.OP_COUNT, table);
    }
}
