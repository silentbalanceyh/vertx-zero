package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

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

    static Post export(final List<String> columnList) {
        return Fn.poolThread(Pooled.POST_MAP, () -> new ExportPost(columnList),
            ExportPost.class.getName() + columnList.hashCode());
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

    static <T> Future<JsonObject> success404Pre() {
        return Ux.future(new JsonObject().put(STATUS, 404));
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
        final JsonObject serializedJ;
        if (input instanceof JsonObject) {
            serializedJ = (JsonObject) input;
        } else {
            serializedJ = Ux.toJson(input, module.getPojo());
        }
        Ix.serializeJ(serializedJ, module);
        return Ux.future(serializedJ);
    }

    static <T> Future<JsonArray> successA(final T input, final KModule module) {
        final JsonArray serializedA;
        if (input instanceof JsonArray) {
            serializedA = (JsonArray) input;
        } else {
            serializedA = Ux.toJson((List<T>) input, module.getPojo());
        }
        Ut.itJArray(serializedA).forEach(json -> Ix.serializeJ(json, module));
        return Ux.future(serializedA);
    }

    Future<T> outAsync(Object active, Object standBy);
}
