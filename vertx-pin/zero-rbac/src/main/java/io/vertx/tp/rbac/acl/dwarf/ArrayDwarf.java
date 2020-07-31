package io.vertx.tp.rbac.acl.dwarf;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.secure.Acl;

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
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonArray inputArray = dataReference.getJsonArray("data");
        /* rows */
        JsonArray updated = Dwarf.onRows(inputArray, matrix.getJsonObject("rows"));
        /* projection: for After Get only */
        updated = Dwarf.onProjection(updated, Sc.aclProjection(matrix.getJsonArray(Inquiry.KEY_PROJECTION), acl));
        /* Updated */
        dataReference.put("data", updated);
    }
}
