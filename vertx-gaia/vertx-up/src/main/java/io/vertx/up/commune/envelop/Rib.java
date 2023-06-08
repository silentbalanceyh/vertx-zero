package io.vertx.up.commune.envelop;

import io.horizon.exception.WebException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KWeb;

public class Rib {

    public static <T> JsonObject input(final T data) {
        return RibTool.input(data);
    }

    public static <T> T deserialize(final Object value, final Class<?> clazz) {
        return RibTool.deserialize(value, clazz);
    }

    public static JsonObject outJson(final JsonObject data, final WebException error) {
        return RibTool.outJson(data, error);
    }

    public static Buffer outBuffer(final JsonObject data, final WebException error) {
        return RibTool.outBuffer(data, error);
    }

    public static JsonObject getBody(final JsonObject data) {
        return RibData.getBody(data);
    }

    public static <T> T get(final JsonObject data) {
        return RibData.get(data);
    }

    public static <T> T get(final JsonObject data, final Class<?> clazz) {
        return RibData.get(data, clazz);
    }

    public static <T> T get(final JsonObject data, final Class<?> clazz, final Integer index) {
        return RibData.get(data, clazz, index);
    }

    public static <T> void set(final JsonObject data, final String field, final T value, final Integer argIndex) {
        RibData.set(data, field, value, argIndex);
    }

    public static boolean isIndex(final Integer argIndex) {
        return KWeb.MULTI.INDEXES.containsKey(argIndex);
    }
}
