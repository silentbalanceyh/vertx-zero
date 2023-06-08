package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.pojo.Mirror;
import io.vertx.up.commune.pojo.Mojo;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class From {

    static <T> T fromJson(final JsonObject data, final Class<T> clazz,
                          final String pojo) {
        return Fn.runOr(Ut.isNil(pojo), null,
            // Turn On Smart Serialization on Business Layer
            () -> Ut.deserialize(data, clazz, true),
            () -> Mirror.create(From.class)
                .mount(pojo)
                .connect(data)
                .type(clazz)
                .from()
                .get());
    }

    @SuppressWarnings("all")
    static <T> List<T> fromJson(final JsonArray data, final Class<?> clazz, final String pojo) {
        final List<T> result = new ArrayList<>();
        Ut.itJArray(data).map(each -> fromJson(each, clazz, pojo))
            .filter(Objects::nonNull)
            .map(item -> (T) item)
            .forEach(result::add);
        return result;
    }

    static JsonObject fromJson(final JsonObject criteria, final String pojo) {
        final Mojo mojo = Mirror.create(From.class).mount(pojo).mojo();
        return JqTool.criteria(criteria, mojo);
    }
}

