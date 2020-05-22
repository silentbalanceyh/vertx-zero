package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

public class IxHttp {

    /* 201 */
    public static <T> Future<Envelop> success201(final T entity) {
        return Ux.future(Envelop.success(entity, HttpStatusCode.CREATED));
    }

    public static <T> Future<Envelop> success201(final T entity, final IxModule config) {
        final JsonObject serializedJson = Ux.toJson(entity, config.getPojo());
        /* metadata must be converted */
        Ke.mount(serializedJson, KeField.METADATA);
        return success201(serializedJson);
    }

    /* 200 */
    public static <T> Future<Envelop> success200(final T entity) {
        return Ux.future(Envelop.success(entity));
    }

    public static <T> Future<Envelop> success200(final T entity, final IxModule config) {
        final JsonObject serializedJson = Ux.toJson(entity, config.getPojo());
        /* metadata must be converted */
        Ke.mount(serializedJson, KeField.METADATA);
        return success200(entity);
    }

    /* 204 */
    public static <T> Future<Envelop> success204(final T entity) {
        return Ux.future(Envelop.success(entity, HttpStatusCode.NO_CONTENT));
    }
}
