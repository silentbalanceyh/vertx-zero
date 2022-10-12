package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnVisit implements Quinn {
    @Override
    public Future<JsonObject> syncAsync(final SResource resource, final ScOwner owner, final JsonObject data) {
        return null;
    }
}
