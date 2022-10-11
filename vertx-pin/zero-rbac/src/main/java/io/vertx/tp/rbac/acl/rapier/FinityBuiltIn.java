package io.vertx.tp.rbac.acl.rapier;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FinityBuiltIn implements Finity {
    @Override
    public Future<JsonObject> restrict(final JsonObject request, final JsonObject syntax) {
        // 标准化执行处理
        final JsonObject exprTpl = Ut.valueJObject(syntax, KName.DATA);
        final JsonObject condition = Ut.fromExpression(exprTpl, request);
        Sc.infoView(this.getClass(), "Visitant unique query condition: {0}", condition);
        return Ux.future(condition);
    }
}
