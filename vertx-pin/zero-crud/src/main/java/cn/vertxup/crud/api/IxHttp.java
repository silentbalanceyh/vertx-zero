package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class IxHttp {

    /* 201 */
    public static <T> Future<Envelop> success201(final T entity) {
        return Ux.future(Envelop.success(entity, HttpStatusCode.CREATED));
    }

    public static <T> Future<Envelop> success201(final T entity, final IxModule config) {
        final String pojo = config.getPojo();
        if (Ut.isNil(pojo)) {
            return success201(entity);
        } else {
            final JsonObject serializedJson = Ux.toJson(entity, pojo);
            return Ux.future(Envelop.success(serializedJson, HttpStatusCode.CREATED));
        }
    }

    /* 200 */
    public static <T> Future<Envelop> success200(final T entity) {
        return Ux.future(Envelop.success(entity));
    }

    public static <T> Future<Envelop> success200(final T entity, final IxModule config) {
        final String pojo = config.getPojo();
        if (Ut.isNil(pojo)) {
            return success200(entity);
        } else {
            final JsonObject serializedJson = Ux.toJson(entity, pojo);
            return Ux.future(Envelop.success(serializedJson));
        }
    }

    /* 204 */
    public static <T> Future<Envelop> success204(final T entity) {
        return Ux.future(Envelop.success(entity, HttpStatusCode.NO_CONTENT));
    }
}
