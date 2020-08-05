package io.vertx.tp.rbac.acl.rapid;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.secure.Acl;

/*
 * Here are the record filter `Dwarf`
 * The data structure is as following:
 * {
 *      "data": {
 *           "field1": "value1",
 *           "field2": "value2",
 *           "field3": "value3"
 *      },
 *      "acl": ???
 * }
 * The impact elements are
 *
 * 1) S_VIEW: projection configuration
 * 2) S_VISITANT: aclVisible configuration
 *
 * Only `projection` is ok for filter the fields and calculate `acl` information.
 *
 * 1. AFTER, EAGER ( only )
 * 2. projection of S_VIEW is high priority and it should be kept;
 * 3. aclVisible is not empty, the system should combine `aclVisible` and `projection` to get the final data
 * 4. Returned:
 * ---- The combined `aclVisible` + `projection` should produce the final data
 * ---- There are additional node `acl` in the same level of `data` that stored into database
 * ---- In front, the `acl` data will be stored to `__acl`
 */
class RecordDwarf implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonObject record = dataReference.getJsonObject("data");

        /* Capture data fields of current record */
        Sc.aclRecord(record, acl);

        /* projection only */
        final JsonObject updated = DataDwarf.onProjection(record,
                Sc.aclOn(matrix.getJsonArray(Inquiry.KEY_PROJECTION), acl));

        /* Updated */
        dataReference.put("data", updated);
    }
}
