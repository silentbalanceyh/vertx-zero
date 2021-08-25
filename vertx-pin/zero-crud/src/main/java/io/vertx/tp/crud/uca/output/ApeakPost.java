package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ApeakPost implements Post<JsonArray> {
    @Override
    public Future<JsonArray> outAsync(final Object columnsObj, final Object linkedObj) {
        // Major Field
        final JsonArray columns = (JsonArray) columnsObj;
        final JsonArray linked = (JsonArray) linkedObj;
        final Set<String> majorSet = this.field(columns);

        final JsonArray filtered = new JsonArray();
        // Duplicated Column Parsing
        final Set<String> fieldSet = new HashSet<>();
        boolean append = true;
        for (int idx = Values.IDX; idx < columns.size(); idx++) {
            final Object value = columns.getValue(idx);
            if (Objects.isNull(value)) {
                // Continue for current loop
                continue;
            }
            if (Constants.DEFAULT_HOLDER.equals(value)) {
                // HOLDER Collection
                linked.forEach(item -> this.add(filtered, item, majorSet));
                append = false;
            } else {
                // Add column here
                this.add(filtered, value, fieldSet);
            }
        }
        if (append && Ut.notNil(linked)) {
            // Append `linked`
            linked.forEach(item -> this.add(filtered, item, majorSet));
        }
        return Ux.future(filtered);
    }

    private void add(final JsonArray columns, final Object value, final Set<String> fieldSet) {
        final String field = this.field(value);
        if (Objects.nonNull(field)) {
            if (!fieldSet.contains(field)) {
                columns.add(value);
                fieldSet.add(field);
            }
        }
    }

    /* field name parsing */
    private String field(final Object value) {
        final String field;
        if (value instanceof String) {
            // metadata
            field = value.toString().split(",")[0];
        } else {
            final JsonObject column = (JsonObject) value;
            if (column.containsKey(KName.METADATA)) {
                // metadata
                final String metadata = column.getString(KName.METADATA);
                if (Ut.notNil(metadata)) {
                    field = metadata.split(",")[0];
                } else {
                    field = null;
                }
            } else {
                // dataIndex
                field = column.getString("dataIndex");
            }
        }
        return field;
    }

    private Set<String> field(final JsonArray columns) {
        final Set<String> fieldSet = new HashSet<>();
        columns.stream().map(this::field).forEach(fieldSet::add);
        return fieldSet;
    }
}
