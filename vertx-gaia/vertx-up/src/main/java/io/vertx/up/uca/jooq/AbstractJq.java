package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.up.log.Annal;

import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Two mode:
 *
 * 1) Dim1: Sync / Async
 * 2) Dim2: Pojo / Bind-Pojo / Non-Pojo
 */
@SuppressWarnings("all")
public abstract class AbstractJq {
    protected transient final VertxDAO vertxDAO;
    protected transient final JqAnalyzer analyzer;

    protected AbstractJq(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
    }

    /*
     * Future processed
     * Callback for it for future processed
     */
    protected <T> Future<T> successed(final CompletableFuture<T> future) {
        final Promise<T> promise = Promise.promise();
        future.thenAcceptAsync(promise::complete)
                .exceptionally((ex) -> failure(ex, promise));
        return promise.future();
    }

    protected <T> Future<T> successed(final CompletableFuture<Void> future, final T input) {
        final Promise<T> promise = Promise.promise();
        future.thenAcceptAsync(nil -> promise.complete(input))
                .exceptionally((ex) -> failure(ex, promise));
        return promise.future();
    }

    protected Void failure(final Throwable ex, final Promise promise) {
        logger().jvm(ex);
        promise.fail(ex);
        return null;
    }

    /**
     * The logger could be used by sub-class only
     */
    protected Annal logger() {
        return Annal.get(getClass());
    }
}
