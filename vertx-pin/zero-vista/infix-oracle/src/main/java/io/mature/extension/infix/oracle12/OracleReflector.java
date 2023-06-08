package io.mature.extension.infix.oracle12;

import io.mature.extension.infix.oracle12.cv.OracleStatement;
import io.mature.extension.infix.oracle12.cv.OracleWord;
import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AbstractReflector;
import io.modello.eon.em.EmKey;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OracleReflector extends AbstractReflector implements OracleStatement, OracleWord {


    OracleReflector(final AoConnection connection) {
        super(connection);
    }

    @Override
    public ConcurrentMap<String, EmKey.Type> getConstraints(final String tableName) {
        final ConcurrentMap<String, EmKey.Type> constraints = new ConcurrentHashMap<>();
        final String sql = MessageFormat.format(OracleStatement.R_CONSTRAINTS, this.connection.getDatabase().getInstance(), tableName);
        final String[] cols = {Metadata.CONSTRAINT_NAME, Metadata.CONSTRAINT_TYPE};
        final List<ConcurrentMap<String, Object>> results = this.connection.select(sql, cols);
        results.forEach(row -> {
            final Object type = row.get(Metadata.CONSTRAINT_TYPE);
            if (null != type) {
                final Object name = row.get(Metadata.CONSTRAINT_NAME);
                if ("U".equalsIgnoreCase(type.toString())) {
                    // Unique约束名
                    constraints.put(name.toString(), EmKey.Type.UNIQUE);
                } else if ("P".equalsIgnoreCase(type.toString())) {
                    // 主键特殊约束名
                    constraints.put(name.toString(), EmKey.Type.PRIMARY);
                }
            }
        });
        return constraints;
    }

    @Override
    public <T> List<T> getColumns(final String tableName) {
        final String sql = MessageFormat.format(OracleStatement.R_COLUMNS, this.connection.getDatabase().getInstance(), tableName);
        return this.connection.select(sql, Metadata.COLUMN);
    }

    @Override
    public List<ConcurrentMap<String, Object>> getColumnDetail(final String tableName) {
        return new ArrayList<>();
    }
}
