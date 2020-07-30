package io.vertx.tp.rbac.acl.dwarf;

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
             * ( In new version, Jooq logical is synced with current logical ), the projection is columns that
             * should be returned in to frontend UI )
             *
             * The original projection means `filter`, but now it means returned projection fields.
             * Current version is correct here, But this feature is only supported when projection is not null.
             *
             * 1) When projection is `[]` ( Empty ), do nothing and returned all columns.
             * 2) When projection is not empty, extract all columns that stored into projection and returned.
             *
             * *: The `key` fields will be stored into `view` table instead of other columns, it stored as default
             * and it means that not needed to extract `key` here.
             */
            final Set<String> projectionSet = new HashSet<>(projection.getList());
            // Old:  fields.stream().filter(projectionSet::contains)
            fields.stream().filter(item -> !projectionSet.contains(item))
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
