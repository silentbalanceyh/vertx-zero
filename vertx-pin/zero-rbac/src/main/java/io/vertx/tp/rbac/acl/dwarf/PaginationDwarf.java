package io.vertx.tp.rbac.acl.dwarf;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Acl;

/*
 * Filters for
 * data -> list [] node
 * rows only
 */
class PaginationDwarf implements DataDwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonObject pagination = dataReference.getJsonObject("data");
        final JsonArray inputArray = pagination.getJsonArray("list");
        /* rows */
        final JsonArray updated = Dwarf.onRows(inputArray, matrix.getJsonObject("rows"));
        /* Updated, be careful there should modify list node */
        pagination.put("list", updated);
    }
}
