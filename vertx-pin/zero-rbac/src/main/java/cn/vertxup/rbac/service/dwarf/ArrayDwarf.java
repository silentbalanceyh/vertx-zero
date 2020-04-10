package cn.vertxup.rbac.service.dwarf;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;

/*
 * rows only
 */
class ArrayDwarf implements DataDwarf {
    /*
     * {
     *     "data": []
     * }
     *
     */
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix) {
        /* inputArray */
        final JsonArray inputArray = dataReference.getJsonArray("data");
        /* rows */
        JsonArray updated = Dwarf.onRows(inputArray, matrix.getJsonObject("rows"));
        /* projection: for After Get only */
        updated = Dwarf.onProjection(updated, matrix.getJsonArray(Inquiry.KEY_PROJECTION));
        /* Updated */
        dataReference.put("data", updated);
    }
}
