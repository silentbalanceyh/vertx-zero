package io.vertx.tp.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OkAApeak implements Co<JsonObject, JsonArray, Object, JsonArray> {
    @Override
    public Future<JsonArray> ok(final JsonArray columns, final Object linkedObj) {
        /*
         * Fix Bug:
         * java.lang.ClassCastException: io.vertx.core.json.JsonObject cannot be cast to io.vertx.core.json.JsonArray
         * Because standard sub-module ignored processing
         */
        final JsonArray linked;
        if (linkedObj instanceof JsonArray) {
            linked = (JsonArray) linkedObj;
        } else {
            linked = new JsonArray();
        }
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


    private Set<String> field(final JsonArray columns) {
        final Set<String> fieldSet = new HashSet<>();
        columns.stream().map(Ix::onColumn)
            .filter(Objects::nonNull)
            .map(Kv::getKey)
            .forEach(fieldSet::add);
        return fieldSet;
    }

    private void add(final JsonArray columns, final Object value, final Set<String> fieldSet) {
        final Kv<String, String> kv = Ix.onColumn(value);
        if (Objects.nonNull(kv)) {
            if (!fieldSet.contains(kv.getKey())) {
                columns.add(value);
                fieldSet.add(kv.getKey());
            }
        }
    }
}
