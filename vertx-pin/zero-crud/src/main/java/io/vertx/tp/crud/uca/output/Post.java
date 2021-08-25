package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Post<T> {

    String STATUS = "$STATUS$";
    String RESULT = "$RESULT$";

    static Post apeak(final boolean isMy) {
        if (isMy) {
            return Fn.poolThread(Pooled.POST_MAP, ApeakMyPost::new, ApeakMyPost.class.getName());
        } else {
            return Fn.poolThread(Pooled.POST_MAP, ApeakPost::new, ApeakPost.class.getName());
        }
    }

    /* STATUS Code */
    static Future<Envelop> successPost(final JsonObject input) {
        final HttpStatusCode statusCode = getStatus(input);
        return Ux.future(Envelop.success(input, statusCode));
    }

    static Future<Envelop> successPostB(final JsonObject input) {
        final HttpStatusCode statusCode = getStatus(input);
        final Boolean result = input.getBoolean(RESULT, Boolean.FALSE);
        return Ux.future(Envelop.success(result, statusCode));
    }

    static HttpStatusCode getStatus(final JsonObject input) {
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

    static <T> Future<JsonObject> success201Pre(final T input, final KModule module) {
        return successJ(input, module).compose(item -> {
            item.put(STATUS, 201);
            return Ux.future(item);
        });
    }

    static <T> Future<JsonObject> success204Pre() {
        return Ux.future(new JsonObject().put(STATUS, 204));
    }

    static Future<JsonObject> success200Pre(final boolean result) {
        return Ux.future(new JsonObject().put(STATUS, 200).put(RESULT, result));
    }

    static Future<JsonObject> success204Pre(final boolean result) {
        return Ux.future(new JsonObject().put(STATUS, 204).put(RESULT, result));
    }

    /*
     *  T -> JsonObject based by module
     */
    static <T> Future<JsonObject> successJ(final T input, final KModule module) {
        final JsonObject serializedJson;
        if (input instanceof JsonObject) {
            serializedJson = (JsonObject) input;
        } else {
            serializedJson = Ux.toJson(input, module.getPojo());
        }
        Ix.serializeJ(serializedJson, module);
        return Ux.future(serializedJson);
    }

    Future<T> outAsync(Object active, Object standBy);
}
