package io.vertx.tp.optic.secure;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.feature.Confine;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfineAction implements Confine {
    @Override
    public Future<JsonObject> restrict(final JsonObject request, final JsonObject syntax) {
        final JsonObject exprTpl = Ut.valueJObject(syntax, KName.DATA);
        final JsonObject condition = Ut.fromExpression(exprTpl, request);
        return Ux.future(condition);
    }
}
