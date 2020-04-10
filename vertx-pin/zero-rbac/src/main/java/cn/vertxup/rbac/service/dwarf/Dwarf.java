package cn.vertxup.rbac.service.dwarf;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.log.Annal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Dwarf {
    private static final Annal LOGGER = Annal.get(Dwarf.class);

    /*
     * projection -> JsonObject
     */
    @SuppressWarnings("all")
    static JsonObject onProjection(final JsonObject input, final JsonArray projection) {
        final Set<String> fields = new HashSet<>(input.fieldNames());
        /*
         * If projection is empty, do nothing
         */
        if (!projection.isEmpty()) {
            Sc.debugAuth(LOGGER, AuthMsg.REGION_PROJECTION, projection.encode());
            /*
             * The method is the same as backend of Jooq
             * Projection means filter
             */
            final Set<String> projectionSet = new HashSet<>(projection.getList());
            fields.stream().filter(projectionSet::contains)
                    .forEach(input::remove);
        }
        return input;
    }

    /*
     * projection -> JsonArray
     */
    static JsonArray onProjection(final JsonArray input, final JsonArray projection) {
        final JsonArray result = new JsonArray();
        input.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .map(item -> onProjection(item, projection))
                .forEach(result::add);
        return result;
    }

    /*
     * rows -> JsonArray
     */
    static JsonArray onRows(final JsonArray input, final JsonObject rows) {
        final JsonArray result = new JsonArray();
        if (rows.isEmpty()) {
            /*
             * Do not do any row filters.
             */
            result.addAll(input);
        } else {
            Sc.infoAuth(LOGGER, AuthMsg.REGION_ROWS, rows.encode());
            input.stream().filter(Objects::nonNull)
                    .map(item -> (JsonObject) item)
                    .filter(item -> isMatch(item, rows))
                    .forEach(result::add);
        }
        return result;
    }

    private static boolean isMatch(final JsonObject item, final JsonObject rows) {
        final long counter = rows.fieldNames().stream().filter(field -> {
            final Object inputValue = item.getValue(field);
            final JsonArray rowsArray = rows.getJsonArray(field);
            return !rowsArray.contains(inputValue);
        }).count();
        return 0 == counter;
    }
}
