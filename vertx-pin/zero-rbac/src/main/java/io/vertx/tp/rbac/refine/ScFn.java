package io.vertx.tp.rbac.refine;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.atom.unity.UArray;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;

class ScFn {

    private static final WebException ERROR = new _500InternalServerException(ScFn.class, null);

    static <T> Future<JsonArray> relation(final String field, final String key, final Class<?> daoCls) {
        return Ux.Jooq.on(daoCls).<T>fetchAsync(field, key)
            .compose(Ux::futureA)
            .compose(relation -> UArray.create(relation)
                .remove(field).toFuture());
    }

    static <T> Future<List<T>> composite(final CompositeFuture res) {
        if (res.succeeded()) {
            final List<T> result = res.list();
            return Future.succeededFuture(result);
        } else {
            final Throwable error = res.cause();
            final WebException failure;
            if (Objects.nonNull(error)) {
                error.printStackTrace();
                failure = new _500InternalServerException(ScFn.class, error.getMessage());
            } else {
                failure = ERROR;
            }
            return Future.failedFuture(failure);
        }
    }
}
