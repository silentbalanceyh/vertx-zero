package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KMetadata;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

class KeElement {

    static Function<JsonObject, Future<JsonObject>> mountArray(final String field) {
        return response -> Ux.future(mountArray(response, field));
    }

    static JsonObject mountArray(final JsonObject response, final String field) {
        return Fn.getJvm(new JsonObject(), () -> {
            final String data = response.getString(field);
            if (Objects.nonNull(data) && Ut.isJArray(data)) {
                response.put(field, new JsonArray(data));
            }
            return response;
        }, response);
    }

    static JsonObject mount(final JsonObject response, final String field) {
        return Fn.getJvm(new JsonObject(), () -> {
            final String data = response.getString(field);
            if (Objects.nonNull(data) && Ut.isJObject(data)) {
                response.put(field, parseMetadata(new JsonObject(data)));
            }
            return response;
        }, response);
    }

    static JsonObject mountString(final JsonObject response, final String field) {
        return Fn.getJvm(new JsonObject(), () -> {
            final Object value = response.getValue(field);
            if (Objects.nonNull(value)) {
                if (value instanceof JsonObject) {
                    response.put(field, ((JsonObject) value).encode());
                } else if (value instanceof JsonArray) {
                    response.put(field, ((JsonArray) value).encode());
                }
            }
            return response;
        }, response);
    }

    static Function<JsonObject, Future<JsonObject>> mount(final String field) {
        return response -> Ux.future(mount(response, field));
    }

    static Function<JsonArray, Future<JsonArray>> mounts(final String field) {
        return response -> {
            Ut.itJArray(response).forEach(json -> mount(json, field));
            return Ux.future(response);
        };
    }

    static Function<JsonObject, Future<JsonObject>> mount(final String... field) {
        return response -> {
            Arrays.stream(field).forEach(each -> mount(response, each));
            return Ux.future(response);
        };
    }

    static Function<JsonArray, Future<JsonArray>> mounts(final String... field) {
        return response -> {
            Arrays.stream(field).forEach(each -> Ut.itJArray(response).forEach(json -> mount(json, each)));
            return Ux.future(response);
        };
    }

    /*
     * Spec metadata data structure of Json normalized.
     */
    private static JsonObject parseMetadata(final JsonObject metadata) {
        assert Objects.nonNull(metadata) : "Here input metadata should not be null";
        /*
         * Structure that will be parsed here.
         */
        return new KMetadata(metadata).toJson();
    }
}
