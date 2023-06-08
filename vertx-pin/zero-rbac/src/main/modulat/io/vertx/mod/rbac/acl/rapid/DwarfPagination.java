package io.vertx.mod.rbac.acl.rapid;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.unity.Ux;

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
class DwarfPagination implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonObject pagination = dataReference.getJsonObject(KName.DATA);
        /* rows */
        Ux.pageData(pagination, inputArray -> SiftRow.onRows(inputArray, matrix.getJsonObject("rows")));
        /* criteria mount appened to
         * {
         *     "count": x,
         *     "list": []
         * }
         * For view of query modification to initializing query form here
         * copy the `criteria` node to response as following:
         * {
         *      "count": x,
         *      "list": []
         * }
         * */
        Dwarf.create().minimize(dataReference, matrix, acl);
        // T.qr(dataReference, matrix);
    }
}
