package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

@Deprecated
public class IxHttp {

    /* 201 */
    public static <T> Future<Envelop> success201(final T entity, final KModule config) {
        final JsonObject serializedJson = Ux.toJson(entity, config.getPojo());
        /* metadata must be converted */
        Ix.serializeJ(serializedJson, config);
        return Ux.future(Envelop.success(serializedJson, HttpStatusCode.CREATED));
    }

    /* 200 */
    public static <T> Future<Envelop> success200(final T entity) {
        return Ux.future(Envelop.success(entity));
    }

    public static <T> Future<Envelop> success200(final T entity, final KModule config) {
        final JsonObject serializedJson = Ux.toJson(entity, config.getPojo());
        /* metadata must be converted */
        Ix.serializeJ(serializedJson, config);
        return Ux.future(Envelop.success(serializedJson));
    }

    /* 204 */
    public static <T> Future<Envelop> success204(final T entity) {
        return Ux.future(Envelop.success(entity, HttpStatusCode.NO_CONTENT));
    }
}
