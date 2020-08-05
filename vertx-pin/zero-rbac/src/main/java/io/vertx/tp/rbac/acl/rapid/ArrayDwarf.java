package io.vertx.tp.rbac.acl.rapid;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.secure.Acl;

import java.util.Objects;

/*
 * Here are the JsonArray list filter `Dwarf`
 * The data structure is as following:
 * {
 *      "data": [
 *          {
 *              "field1": "value1",
 *              "field2": "value2",
 *              "field3": "value3",
 *          }
 *      ],
 *      "acl": ???
 * }
 * The impact elements are
 *
 * 1) S_VIEW: projection / rows configuration
 * 2) S_VISITANT: aclVisible configuration
 *
 * 1. AFTER, EAGER ( only )
 * 2. The `rows` and `projection` of S_VIEW are high priority here.
 * 3. aclVisible could be used:
 * ---- projection directly without any configuration defined in `seeker syntax`.
 * ---- rows filtering with configuration defined in `seeker syntax`.
 */
class ArrayDwarf implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonArray inputArray = dataReference.getJsonArray("data");

        /* rows */
        JsonArray updated = DataDwarf.onRows(inputArray, matrix.getJsonObject("rows"));

        /* projection: for After Get only */
        updated = DataDwarf.onProjection(updated, matrix.getJsonArray(Inquiry.KEY_PROJECTION));

        if (Objects.nonNull(acl)) {
            /* acl normalized on */
        }
        /* Updated */
        dataReference.put("data", updated);
    }
}
