package io.vertx.tp.rbac.acl.rapid;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.RegionType;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<RegionType, Dwarf> DWARF_POOL =
        new ConcurrentHashMap<>();
}

class T {
    /*
     * The standard response is as following:
     * {
     *      "data": {
     *          "list": [],
     *          "count": x
     *      },
     *      "acl": {
     *      }
     * }
     * The specification for response is as following:
     * {
     *      "data": {
     *          "list": [],
     *          "count": x
     *      },
     *      "acl": {
     *      },
     *      "qr": {
     *
     *      }
     * }
     * Append matrix data into view node, here are following attributes in view matrix in backend
     * 1. criteria: Here put the value into "qr" node.
     * 2. 「Not Need」projection: Because the projection could be calculated based on `/column/full` and `/column/my` in
     * frontend, in this kind of situation, it could be KO.
     * 3. 「Not Need」rows：The rows impaction is on response result, it could be KO.
     *
     * Attention:
     * 1. qr contains two parts:
     * - `criteria` of request
     * - `criteria` defined in view
     * Here only put `criteria` definition in view into 'qr' node.
     * This operation is in ARRAY, PAGINATION kind of dwarf
     */
    static void qr(final JsonObject response, final JsonObject matrix) {
        final JsonObject query = matrix.getJsonObject(Qr.KEY_CRITERIA);
        if (Ut.notNil(query)) {
            response.put("qr", query);
        }
    }
}