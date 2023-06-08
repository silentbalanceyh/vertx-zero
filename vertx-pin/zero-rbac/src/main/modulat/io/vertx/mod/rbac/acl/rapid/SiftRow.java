package io.vertx.mod.rbac.acl.rapid;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SiftRow {

    private static final Annal LOGGER = Annal.get(SiftRow.class);

    static JsonObject onAcl(final JsonObject rows, final Set<String> visible) {
        final JsonObject result = new JsonObject();
        final JsonArray fields = Ut.valueJArray(rows.getJsonArray("condition"));
        if (Ut.isNotNil(fields)) {
            Ut.itJArray(fields, String.class, (field, index) -> {
                final JsonArray visibleFields = Ut.toJArray(visible);
                result.put(field, visibleFields);
            });
            return result;
        } else {
            return null;
        }
    }

    /*
     * rows -> JsonArray
     */
    static JsonArray onRows(final JsonArray input, final JsonObject rows) {
        final JsonArray result = new JsonArray();
        final JsonObject rowData = Ut.valueJObject(rows);
        if (rowData.isEmpty()) {
            /*
             * Do not do any row filters.
             */
            result.addAll(input);
        } else {
            LOG.Auth.info(LOGGER, AuthMsg.REGION_ROWS, rowData.encode());
            input.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .filter(item -> isMatch(item, rowData))
                .forEach(result::add);
        }
        return result;
    }

    private static boolean isMatch(final JsonObject item, final JsonObject rows) {
        /*
         * Multi fields here
         */
        return rows.fieldNames().stream().anyMatch(field -> {
            final Object inputValue = item.getValue(field);
            final JsonArray rowsArray = rows.getJsonArray(field);
            return rowsArray.contains(inputValue);
        });
    }
}
