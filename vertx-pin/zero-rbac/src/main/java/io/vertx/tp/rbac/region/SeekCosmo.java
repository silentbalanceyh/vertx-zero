package io.vertx.tp.rbac.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class SeekCosmo implements Cosmo {
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final String component = seeker.getString("component");
        if (Ut.notNil(component)) {
            final Cosmo external = Ut.singleton(component);
            return external.before(request, matrix);
        } else {
            return DataIn.visitCond(request, matrix).compose(acl -> {
                /*
                 * Read AclData
                 */
                request.acl(acl);
                return null;
            });
        }
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        final JsonObject seeker = matrix.getJsonObject("seeker");
        final String component = seeker.getString("component");
        if (Ut.notNil(component)) {
            final Cosmo external = Ut.singleton(component);
            return external.after(response, matrix);
        } else {
            return null;
        }
    }
}
