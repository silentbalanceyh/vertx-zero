package io.vertx.mod.rbac.acl.rapid;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.util.Ut;

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
class DwarfArray implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonArray inputArray = dataReference.getJsonArray("data");

        /* rows */
        JsonArray updated = SiftRow.onRows(inputArray, matrix.getJsonObject("rows"));

        if (Objects.nonNull(acl)) {
            /*
             * Acl normalized only
             * 2 -> 1 choice
             * 1) Existing config: aclVisible -> rows
             * 2) No config: aclVisible as projection
             */
            final JsonObject config = acl.config();
            if (Ut.isNil(config) || Ut.isNil(config.getJsonObject("rows"))) {

                /* projection: for After Get only */
                updated = SiftCol.onProjection(updated,
                    Sc.aclOn(matrix.getJsonArray(Ir.KEY_PROJECTION), acl));
            } else {

                /* pick up projection in S_VIEW only */
                updated = SiftCol.onProjection(updated,
                    matrix.getJsonArray(Ir.KEY_PROJECTION));

                /*
                 * Produce rows configuration
                 * {
                 *     "field1": [],
                 *     "field2": [],
                 * }
                 * */
                final JsonObject rows = SiftRow.onAcl(
                    config.getJsonObject("rows"), acl.aclVisible());
                if (Objects.nonNull(rows)) {
                    updated = SiftRow.onRows(updated, rows);
                }
            }
        }
        /* Updated */
        dataReference.put("data", updated);

        Dwarf.create().minimize(dataReference, matrix, acl);
        // T.qr(dataReference, matrix);
    }
}
