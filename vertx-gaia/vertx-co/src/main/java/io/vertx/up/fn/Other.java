package io.vertx.up.fn;

import io.vertx.core.Future;
import io.horizon.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.log.DevEnv;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 专用于 Future 中的 otherwise 部分
 *
 * @author lang : 2023/4/27
 */
final class Other {

    static <T> Function<Throwable, T> otherwise(final Supplier<T> supplier) {
        return error -> {
            if (Objects.nonNull(error)) {
                error.printStackTrace();
            }
            return supplier.get();
        };
    }

    static <T> Future<T> otherwise(final Class<?> target, final Throwable error) {
        final WebException failure;
        if (Objects.isNull(error)) {
            // 异常为 null
            failure = new _500InternalServerException(target, "Otherwise Web Error without Throwable!");
        } else {
            if (DevEnv.devJvmStack()) {
                error.printStackTrace();
            }
            if (error instanceof final WebException webException) {
                // 异常为 WebException
                failure = webException;
            } else {
                // 其他异常，做 WebException 封装
                failure = new _500InternalServerException(target, error.getMessage());
            }
        }
        return Future.failedFuture(failure);
    }

    static <T> Future<T> otherwise(final Class<? extends WebException> clazz, final Object... args) {
        final WebException error = Ut.toError(clazz, args);
        return Future.failedFuture(error);
    }
}
