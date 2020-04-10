package cn.vertxup.rbac.service.dwarf;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;

/*
 * projection only
 */
class RecordDwarf implements DataDwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix) {
        /* inputArray */
        final JsonObject record = dataReference.getJsonObject("data");
        /* projection only */
        final JsonObject updated = Dwarf.onProjection(record, matrix.getJsonArray(Inquiry.KEY_PROJECTION));
        /* Updated */
        dataReference.put("data", updated);
    }
}
