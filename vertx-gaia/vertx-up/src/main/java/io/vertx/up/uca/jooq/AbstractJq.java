package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.List;
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
     * Input / Output
     */
    protected <T> T in(final JsonObject data, final String pojo) {
        return Ux.<T>fromJson(data, (Class<T>) this.analyzer.type(), pojo);
    }

    protected <T> List<T> in(final JsonArray data, final String pojo) {
        return Ux.fromJson(data, (Class<T>) this.analyzer.type(), pojo);
    }

    protected <T> JsonObject outJ(final T entity, final String pojo) {
        return Ux.toJson(entity, pojo);
    }

    protected <T> JsonObject outJ(final T entity) {
        return this.outJ(entity, this.analyzer.pojoFile());
    }

    protected <T> Future<JsonObject> outJAsync(final T entity, final String pojo) {
        return Future.succeededFuture(Ux.toJson(entity, pojo));
    }

    protected <T> Future<JsonObject> outJAsync(final T entity) {
        return outJAsync(entity, this.analyzer.pojoFile());
    }

    /*
     * Future processed
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

    protected Annal logger() {
        return Annal.get(getClass());
    }
}
