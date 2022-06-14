package cn.vertxup.rbac.service.view.source;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RadixTool {

    static Future<JsonObject> toResponse(final JsonArray response) {
        return Ux.future(response).compose(Ut.ifJArray(KName.METADATA)).compose(data -> {
            final JsonObject normalized = new JsonObject();
            normalized.put(KName.DATUM, data);
            return Ux.future(normalized);
        });
    }

    static UxJooq toDao(final String daoClsStr) {
        if (Ut.isNil(daoClsStr)) {
            return null;
        } else {
            final Class<?> daoCls = Ut.clazz(daoClsStr, null);
            if (Objects.isNull(daoCls)) {
                return null;
            } else {
                return Ux.Jooq.on(daoCls);
            }
        }
    }
}
