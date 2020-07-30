package io.vertx.tp.rbac.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class SeekCosmo implements Cosmo {
    @Override
    public Future<Envelop> before(final Envelop request, final JsonObject matrix) {
        /*
         *  Cosmo extraction
         */
        return DataIn.visitCond(request, matrix).compose(acl -> {
            return null;
        });
    }

    @Override
    public Future<Envelop> after(final Envelop response, final JsonObject matrix) {
        return Ux.future(response);
    }
}
