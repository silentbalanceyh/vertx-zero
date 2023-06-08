package io.vertx.mod.rbac.atom.acl;

import io.vertx.core.json.JsonObject;
import io.vertx.up.specification.secure.AclView;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclMap extends AbstractAcl {

    private final ConcurrentMap<String, AclView> map = new ConcurrentHashMap<>();

    public AclMap(final String field, final boolean view, final JsonObject config) {
        super(field, view);
        Ut.<JsonObject>itJObject(config, (value, childField) -> {
            final AclView item = new AclItem(childField, value);
            this.map.put(childField, item);
        });
    }

    @Override
    public boolean isAccess() {
        return true;    // Must be access
    }

    @Override
    public ConcurrentMap<String, AclView> complexMap() {
        return this.map;
    }
}
