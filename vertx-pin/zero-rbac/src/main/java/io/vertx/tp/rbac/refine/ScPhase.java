package io.vertx.tp.rbac.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * Uri Processing to calculate resource key here.
 */
class ScPhase {

    static Future<JsonObject> cacheBound(final RoutingContext context, final Envelop envelop) {
        final String habit = envelop.token(KName.HABITUS);
        if (Ut.isNil(habit)) {
            /*
             * Empty bound in current interface instead of other
             */
            return Ux.future(new JsonObject());
        } else {
            final String viewKey = Ke.keyView(context);
            return ScUser.logged(habit).view(viewKey);
        }
    }
}
