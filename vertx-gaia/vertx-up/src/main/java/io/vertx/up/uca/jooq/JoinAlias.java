package io.vertx.up.uca.jooq;

import io.horizon.eon.VString;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SQL AS
 * 1. When renamed from `A` AS `B`
 * 2. One column may contain more than one alias
 *
 * It means that here are the structure of vector such as:
 *
 * field -> column
 * alias -> field -> column
 *
 * 1. You can set `alias` to condition of query engine
 * 2. You can set `alias` to expand query result
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JoinAlias {
    /*
     * Field map
     * field -> table
     * alias -> table
     */
    private transient final ConcurrentMap<String, String> fieldTable
        = new ConcurrentHashMap<>();

    /*
     * Field map of definition
     * field -> Jooq Field
     * alias -> Jooq Field
     */
    private transient final ConcurrentMap<String, Field> fieldMap
        = new ConcurrentHashMap<>();

    /*
     * Column Alias map
     * column -> alias1, alias2, alias3, ...
     *
     * 1. Standard mode: Column -> Field
     * 2. Advanced mode: Column -> Field, Alias1, ...
     */
    private transient final ConcurrentMap<String, Set<String>> columnField
        = new ConcurrentHashMap<>();

    Field field(final String field, final String prefix) {
        final Field original = this.fieldMap.get(field);
        return DSL.field(prefix + "." + original.getName());
    }

    Field field(final String field) {
        final Field found = this.fieldMap.get(field);
        return Objects.isNull(found) ? null : found;
    }

    String table(final String field) {
        return this.fieldTable.getOrDefault(field, VString.EMPTY);
    }

    ConcurrentMap<String, Set<String>> mapColumn() {
        return this.columnField;
    }

    void addDao(final JqAnalyzer analyzer, final String table, final String tableAlias) {
        final ConcurrentMap<String, Field> fields = analyzer.columns();
        for (final String fieldName : fields.keySet()) {
            final Field field = fields.get(fieldName);
            this.fieldMap.put(fieldName, field);
            /*
             * Column Field here
             */
            if (!this.fieldTable.containsKey(field.getName())) {
                this.fieldTable.put(field.getName(), tableAlias);
                /*
                 * Prefix.Column -> Field
                 */
                this.addAlias(table, field, fieldName);
            }
        }
    }

    void addAlias(final JqAnalyzer analyzer, final String table, final String field, final String alias) {
        final Field column = analyzer.column(field);
        this.addAlias(table, column, alias);
        // Another two map should put
        this.fieldMap.put(alias, column);
    }

    private void addAlias(final String table, final Field column, final String alias) {
        final String key = '"' + table + "\".\"" + column.getName() + '"';
        // Extract Set<String>
        final String hashKey = key.toUpperCase();
        final Set<String> fields;
        if (columnField.containsKey(hashKey)) {
            fields = columnField.get(hashKey);
        } else {
            fields = new HashSet<>();
            columnField.put(hashKey, fields);
        }
        // Put Call
        fields.add(alias);
        this.columnField.put(hashKey, fields);
    }
}
