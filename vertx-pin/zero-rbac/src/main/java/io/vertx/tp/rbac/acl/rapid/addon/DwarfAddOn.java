package io.vertx.tp.rbac.acl.rapid.addon;

import io.horizon.specification.zero.secure.Acl;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.acl.rapid.Dwarf;

public class DwarfAddOn implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        // DwarfQr
        Dwarf.create(DwarfQr.class).minimize(dataReference, matrix, acl);
    }
}
