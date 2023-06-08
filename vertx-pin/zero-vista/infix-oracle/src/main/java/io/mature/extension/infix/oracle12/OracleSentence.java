package io.mature.extension.infix.oracle12;


import cn.vertxup.atom.domain.tables.pojos.MField;
import com.hazelcast.internal.util.StringUtil;
import io.mature.extension.infix.oracle12.cv.OracleStatement;
import io.mature.extension.infix.oracle12.cv.OracleWord;
import io.modello.atom.app.KDatabase;
import io.modello.dynamic.modular.jdbc.DataConnection;
import io.modello.dynamic.modular.metadata.AbstractSentence;
import io.vertx.mod.atom.cv.sql.SqlWord;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OracleSentence extends AbstractSentence implements OracleStatement, OracleWord {

    /**
     * 带Pattern的列映射：PRECISION
     **/
    private static final ConcurrentMap<String, String> PRECISION_MAP = new ConcurrentHashMap<>();
    /**
     * 带Pattern的列映射：LENGTH
     **/
    private static final ConcurrentMap<String, String> LENGTH_MAP = new ConcurrentHashMap<>();


    /* 设置行映射 **/
    static {
        /* 精度映射 **/
        PRECISION_MAP.put(Type.DECIMAL, Pattern.P_DECIMAL);
        PRECISION_MAP.put(Type.NUMERIC, Pattern.P_NUMERIC);
        /* 长度映射 **/
        LENGTH_MAP.put(Type.CHAR, Pattern.P_CHAR);
        LENGTH_MAP.put(Type.NCHAR, Pattern.P_NCHAR);
        LENGTH_MAP.put(Type.VARCHAR, Pattern.P_VARCHAR);
        LENGTH_MAP.put(Type.NVARCHAR, Pattern.P_NVARCHAR);
        LENGTH_MAP.put(Type.BINARY, Pattern.P_BINARY);
        LENGTH_MAP.put(Type.NUMERIC, Pattern.P_NUMERICLEN);
    }

    OracleSentence(final KDatabase database) {
        super(database);
    }

    @Override
    public ConcurrentMap<String, String> getPrecisionMap() {
        return PRECISION_MAP;
    }

    @Override
    public ConcurrentMap<String, String> getLengthMap() {
        return LENGTH_MAP;
    }

    @Override
    public String expectTable(final String tableName) {
        return MessageFormat.format(OracleStatement.E_TABLE, this.database.getInstance(), tableName);
    }

    /**
     * 读取包装列的字符
     * NAME -> `NAME`：MySQL
     */
    @Override
    public String columnDdl(final String column) {
        return '"' + column + '"';
    }

    /**
     * 生成列定义的SQL语句："NAME" VARCHAR(255) NOT NULL
     */
    @Override
    public String segmentField(final MField field) {
        final StringBuilder segment = new StringBuilder(this.defineColumn(field));
        // 是否为空检查
        if (!field.getIsNullable() || field.getIsPrimary()) {
            segment.append(SqlWord.Comparator.NOT).append(" ").append(SqlWord.Comparator.NULL);
        } else {
            segment.append(SqlWord.Comparator.NULL);
        }
        return segment.toString();
    }

    // 为带括号的类型提供默认值，所以重写这个方法
    @Override
    protected String getType(final MField field) {
        final StringBuilder type = new StringBuilder();
        final String rawType = this.typeProvider.getColumnType(field.getColumnType());
        // 判断当前类型是否包含了括号
        final String actualType = rawType;

        type.append(this.defineSuffix(field, actualType));
        return type.toString();
    }

    // 如果数据库中字段 nullable 属性和当前一致的情况, 不处理nullable 部分
    private Boolean updateNullable(final String tableName, final MField field) {
        final DataConnection conn = new DataConnection(this.database);
        final String sql = MessageFormat.format(OracleStatement.R_COLUMNS_NULLABLE, conn.getDatabase().getInstance(), tableName, field.getColumnName());
        final List<String> nullable = conn.select(sql, Metadata.NULLABLE);
        final String result = nullable.get(0);
        // 一致的情况，不发送 nullable 语句
        return ((StringUtil.equalsIgnoreCase(result, "Y") && field.getIsNullable())
            || (StringUtil.equalsIgnoreCase(result, "N") && !field.getIsNullable()))
            ? Boolean.FALSE
            : Boolean.TRUE;
    }

    @Override
    public String constraintDrop(final String tableName, final String constraintName) {
        return MessageFormat.format(OracleStatement.ATDC_CONSTRAINT, tableName, constraintName);
    }

    @Override
    public String columnAlter(final String tableName, final MField field) {
        final StringBuilder segment = new StringBuilder(this.defineColumn(field));
        if (this.updateNullable(tableName, field)) {
            if (!field.getIsNullable()) {
                segment.append(SqlWord.Comparator.NOT).append(" ").append(SqlWord.Comparator.NULL);
            } else {
                segment.append(SqlWord.Comparator.NULL);
            }
        }
        return MessageFormat.format(OracleStatement.ATMC_COLUMN, tableName, segment.toString());
    }
}
