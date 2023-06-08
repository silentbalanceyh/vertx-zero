package io.mature.extension.infix.mysql5;

import io.mature.extension.infix.mysql5.cv.MySqlStatement;
import io.mature.extension.infix.mysql5.cv.MySqlWord;
import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AbstractReflector;
import io.modello.eon.em.EmKey;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MySqlReflector extends AbstractReflector implements MySqlStatement, MySqlWord {


    MySqlReflector(final AoConnection connection) {
        super(connection);
    }

    @Override
    public ConcurrentMap<String, EmKey.Type> getConstraints(final String tableName) {
        final ConcurrentMap<String, EmKey.Type> constraints = new ConcurrentHashMap<>();
        final String sql = MessageFormat.format(MySqlStatement.R_CONSTRAINTS, this.connection.getDatabase().getInstance(), tableName);
        final String[] cols = {Metadata.CONSTRAINT_NAME, Metadata.CONSTRAINT_TYPE};
        final List<ConcurrentMap<String, Object>> results = this.connection.select(sql, cols);
        results.forEach(row -> {
            final Object type = row.get(Metadata.CONSTRAINT_TYPE);
            if (null != type) {
                final Object name = row.get(Metadata.CONSTRAINT_NAME);
                if ("UNIQUE".equalsIgnoreCase(type.toString())) {
                    // Unique约束名
                    constraints.put("KEY " + name.toString(), EmKey.Type.UNIQUE);
                } else if ("PRIMARY KEY".equalsIgnoreCase(type.toString())) {
                    // 主键特殊约束名
                    constraints.put("PRIMARY KEY", EmKey.Type.PRIMARY);
                }
            }
        });
        return constraints;
    }

    @Override
    public <T> List<T> getColumns(final String tableName) {
        final String sql = MessageFormat.format(MySqlStatement.R_COLUMNS, this.connection.getDatabase().getInstance(), tableName);
        return this.connection.select(sql, Metadata.COLUMN);
    }

    @Override
    public List<ConcurrentMap<String, Object>> getColumnDetail(final String tableName) {
        final String sql = MessageFormat.format(MySqlStatement.R_COLUMNS_DETAILS, this.connection.getDatabase().getInstance(), tableName);
        final String[] cols = {Metadata.COLUMN, Metadata.DATA_TYPE, Metadata.CHARACTER_LENGTH, Metadata.NUMERIC_PRECISION, Metadata.NUMERIC_SCALE};
        final List<ConcurrentMap<String, Object>> results = this.connection.select(sql, cols);
        return results;
    }

    @Override
    public String getFieldType(final ConcurrentMap<String, Object> columnDetail) {
        // 数据库真实信息
        final String DBType = columnDetail.get(Metadata.DATA_TYPE).toString();
        final String length = columnDetail.get(Metadata.CHARACTER_LENGTH).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.CHARACTER_LENGTH).toString();
        final String precision = columnDetail.get(Metadata.NUMERIC_PRECISION).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.NUMERIC_PRECISION).toString();
        final String scale = columnDetail.get(Metadata.NUMERIC_SCALE).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.NUMERIC_SCALE).toString();
        final String result;
        switch (DBType.toUpperCase()) {
            case MySqlWord.Type.VARCHAR:
            case MySqlWord.Type.CHAR:
                result = DBType + "(" + length + ")";
                break;
            case MySqlWord.Type.DECIAML:
            case MySqlWord.Type.NUMERIC:
                result = DBType + "(" + precision + "," + scale + ")";
                break;
            default:
                result = DBType;
                break;
        }
        return result;
    }

    @Override
    public ConcurrentMap<String, Object> getColumnDetails(final String column, final List<ConcurrentMap<String, Object>> columnDetailList) {
        return columnDetailList
            .stream()
            .filter(item -> {
                for (final Map.Entry<String, Object> col : item.entrySet()) {
                    if (col.getKey().equalsIgnoreCase(Metadata.COLUMN)) {
                        return col.getValue().toString().equalsIgnoreCase(column);
                    }
                }
                return false;
            })
            .findFirst().orElse(new ConcurrentHashMap<>());
    }

    @Override
    public String getDataTypeWord() {
        return Metadata.DATA_TYPE;
    }

    @Override
    public String getLengthWord() {
        return Metadata.CHARACTER_LENGTH;
    }
}
