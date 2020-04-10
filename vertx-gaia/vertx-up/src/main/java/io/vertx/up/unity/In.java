package io.vertx.up.unity;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("all")
class In {

    static <T> T request(final Message<Envelop> message, final Class<T> clazz) {
        final Envelop body = message.body();
        return request(body, clazz);
    }

    static <T> T request(final Envelop envelop, final Class<T> clazz) {
        return Fn.getSemi(null == envelop, null, () -> null, () -> envelop.data(clazz));
    }

    static <T> T request(final Message<Envelop> message, final Integer index, final Class<T> clazz) {
        final Envelop body = message.body();
        return request(body, index, clazz);
    }

    static <T> T request(final Envelop envelop, final Integer index, final Class<T> clazz
    ) {
        return Fn.getSemi(null == envelop, null, () -> null, () -> envelop.data(index, clazz));
    }

    static String requestUser(final Message<Envelop> message, final String field
    ) {
        return requestUser(message.body(), field);
    }

    static String requestUser(final Envelop envelop, final String field) {
        return Fn.getSemi(null == envelop, null, () -> null,
                () -> envelop.identifier(field));
    }

    static String requestTokenData(final String tokenString, final String field) {
        String result = null;
        if (Ut.notNil(tokenString)) {
            final JsonObject token = UxJwt.extract(tokenString);
            if (Objects.nonNull(token)) {
                result = token.getString(field);
            }
        }
        return result;
    }

    static Object requestSession(
            final Message<Envelop> message,
            final String field
    ) {
        return requestSession(message.body(), field);
    }

    static Object requestSession(
            final Envelop envelop,
            final String field
    ) {
        return Fn.getSemi(null == envelop, null, () -> null,
                () -> {
                    final Session session = envelop.getSession();
                    return null == session ? null : session.get(field);
                });
    }

    static JsonArray assignValue(
            final JsonArray source,
            final JsonArray target,
            final String field,
            final boolean override
    ) {
        Ut.itJArray(source, JsonObject.class, (item, index) -> {
            if (override) {
                item.put(field, target.getValue(index));
            } else {
                if (!item.containsKey(field)) {
                    item.put(field, target.getValue(index));
                }
            }
        });
        return source;
    }

    static void assignAuditor(final Object reference, final boolean isUpdate) {
        if (Objects.nonNull(reference) && reference instanceof Envelop) {
            final Envelop envelop = (Envelop) reference;
            final String user = requestUser(envelop, "user");
            if (isUpdate) {
                envelop.setValue("updateBy", user);
                envelop.setValue("udpateTime", Instant.now());
            } else {
                envelop.setValue("key", UUID.randomUUID().toString());
                envelop.setValue("createBy", user);
                envelop.setValue("createTime", Instant.now());
            }
        }
    }
}
