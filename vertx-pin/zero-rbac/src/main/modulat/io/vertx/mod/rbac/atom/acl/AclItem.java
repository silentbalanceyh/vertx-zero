package io.vertx.mod.rbac.atom.acl;

import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclItem extends AbstractAcl {
    private final boolean access;

    public AclItem(final String field, final boolean view, final boolean access) {
        super(field, view);
        this.access = access;
    }

    public AclItem(final String field, final JsonObject input) {
        /*
         * Default access, true
         * Default readonly, false
         */
        this(field, input.getBoolean("visible", Boolean.TRUE),
            input.getBoolean("view", Boolean.FALSE)
        );
    }

    @Override
    public boolean isAccess() {
        return this.access;
    }
}
