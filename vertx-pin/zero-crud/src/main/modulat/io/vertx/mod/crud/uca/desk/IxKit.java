package io.vertx.mod.crud.uca.desk;

import io.aeon.experiment.specification.KModule;
import io.horizon.eon.em.web.HttpStatusCode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public final class IxKit {

    private static String STATUS = "$STATUS$";
    private static String RESULT = "$RESULT$";

    /* STATUS Code */
    public static Future<Envelop> successPost(final JsonObject input) {
        final HttpStatusCode statusCode = getStatus(input);
        return Ux.future(Envelop.success(input, statusCode));
    }

    public static Future<Envelop> successPostB(final JsonObject input) {
        final HttpStatusCode statusCode = getStatus(input);
        final Boolean result = input.getBoolean(RESULT, Boolean.FALSE);
        return Ux.future(Envelop.success(result, statusCode));
    }

    public static HttpStatusCode getStatus(final JsonObject input) {
        final HttpStatusCode statusCode;
        if (input.containsKey(STATUS)) {
            final int status = input.getInteger(STATUS);
            statusCode = HttpStatusCode.fromCode(status);
            input.remove(STATUS);
        } else {
            statusCode = HttpStatusCode.OK;
        }
        return statusCode;
    }

    public static <T> Future<JsonObject> success201Pre(final T input, final KModule module) {
        return successJ(input, module).compose(item -> {
            item.put(STATUS, 201);
            return Ux.future(item);
        });
    }

    public static <T> Future<JsonObject> success204Pre() {
        return Ux.future(new JsonObject().put(STATUS, 204));
    }

    public static <T> Future<JsonObject> success404Pre() {
        return Ux.future(new JsonObject().put(STATUS, 404));
    }

    public static Future<JsonObject> success200Pre(final boolean result) {
        return Ux.future(new JsonObject().put(STATUS, 200).put(RESULT, result));
    }

    public static Future<JsonObject> success204Pre(final boolean result) {
        return Ux.future(new JsonObject().put(STATUS, 204).put(RESULT, result));
    }

    /*
     *  T -> JsonObject based by module
     */
    public static <T> Future<JsonObject> successJ(final T input, final KModule module) {
        return Ux.future(Ix.serializeJ(input, module));
    }

    public static <T> Future<JsonArray> successA(final List<T> input, final KModule module) {
        return Ux.future(Ix.serializeA(input, module));
    }

    public static Future<JsonArray> ignoreA(final JsonObject input, final IxMod in) {
        return Ux.futureA();
    }
}
