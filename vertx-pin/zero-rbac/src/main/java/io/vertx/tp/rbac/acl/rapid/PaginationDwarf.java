package io.vertx.tp.rbac.acl.rapid;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Acl;

/*
 * Here are the pagination list filter `Dwarf`
 * The data structure is as following:
 * {
 *      "data": {
 *          "count": xx,
 *          "list": [
 *              {
 *                  "field1": "value1",
 *                  "field2": "value2",
 *                  "field3": "value3",
 *              }
 *          ]
 *      },
 *      "acl": ???
 * }
 * The impact elements are
 *
 * 1) S_VIEW: rows configuration
 *
 * *: Because projection of S_VIEW will be consider as input request parameters in this kind
 * of situation, it means that in current Dwarf, the projection is not needed.
 *
 * 1. AFTER, EAGER ( only )
 * 2. rows of S_VIEW is high priority and it should be kept;
 * 3. Returned:
 * ---- There is no combine for `acl` information in this kind because there are only two
 *      situations on
 *          BEFORE: projection is ok
 *          AFTER:  rows is ok
 * 4. In future, we could expand the `rows` calculation from here for complex usage
 */
class PaginationDwarf implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonObject pagination = dataReference.getJsonObject("data");
        final JsonArray inputArray = pagination.getJsonArray("list");

        /* rows */
        final JsonArray updated = DataDwarf.onRows(inputArray, matrix.getJsonObject("rows"));

        /* Updated, be careful there should modify list node */
        pagination.put("list", updated);
    }
}
