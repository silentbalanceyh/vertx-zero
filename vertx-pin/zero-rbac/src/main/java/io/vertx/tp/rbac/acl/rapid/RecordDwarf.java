package io.vertx.tp.rbac.acl.rapid;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.secure.Acl;

/*
 * projection only
 */
class RecordDwarf implements DataDwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        /* inputArray */
        final JsonObject record = dataReference.getJsonObject("data");
        /* Capture data fields of current record */
        Sc.aclRecord(record, acl);
        /* projection only */
        final JsonObject updated = Dwarf.onProjection(record,
                Sc.aclAfter(matrix.getJsonArray(Inquiry.KEY_PROJECTION), acl));
        /* Updated */
        dataReference.put("data", updated);
    }
}
