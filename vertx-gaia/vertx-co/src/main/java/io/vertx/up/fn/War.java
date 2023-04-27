package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._400SigmaMissingException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class War {

    private static final WebException ERROR = new _500InternalServerException(War.class, null);

    private static <T> Function<Throwable, T> otherwise(final Supplier<T> supplier) {
        return error -> {
            if (Objects.nonNull(error)) {
                error.printStackTrace();
            }
            return supplier.get();
        };
    }


    static <T> Future<T> thenError(final Class<? extends WebException> clazz, final Object... args) {
        final WebException error = Ut.toError(clazz, args);
        return Future.failedFuture(error);
    }

    static <T> Future<T> thenError(final Class<?> clazz, final String sigma, final Supplier<Future<T>> supplier) {
        if (Ut.isNil(sigma)) {
            final WebException error = new _400SigmaMissingException(clazz);
            return Future.failedFuture(error);
        } else {
            return supplier.get();
        }
    }

}
