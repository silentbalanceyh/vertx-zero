package io.vertx.mod.rbac.acl.rapid.addon;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.rapid.Dwarf;
import io.vertx.up.specification.secure.Acl;

public class DwarfAddOn implements Dwarf {
    @Override
    public void minimize(final JsonObject dataReference, final JsonObject matrix, final Acl acl) {
        // DwarfQr
        Dwarf.create(DwarfQr.class).minimize(dataReference, matrix, acl);
    }
}
