package io.vertx.up.uca.jooq.cache;

import io.horizon.fn.ErrorSupplier;
import io.vertx.core.Future;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.cache.Harp;
import io.vertx.up.plugin.cache.hit.CMessage;
import io.vertx.up.plugin.cache.l1.L1Cache;
import io.vertx.up.plugin.cache.util.CacheAside;
import io.vertx.up.uca.jooq.JqAnalyzer;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * L1 Cache abstract class
 */
public class L1Aside {
    protected final transient JqAnalyzer analyzer;

    protected final transient L1Cache cacheL1;

    public L1Aside(final JqAnalyzer analyzer) {
        /*
         * Dao / Analyzer
         */
        this.analyzer = analyzer;
        /*
         * Vertx Processing
         */
        this.cacheL1 = Harp.cacheL1();
    }

    // ------------------ Private Processing -------------------------
    /*
     * 1) writeCache
     * 2) deleteCache
     * 2) wrapRead/wrapReadAsync
     */
    /*
     * Private method for write
     */
    private void writeCache(final CMessage message) {
        if (Objects.nonNull(this.cacheL1)) {
            this.cacheL1.write(message);
        }
    }

    void deleteCache(final List<CMessage> message) {
        if (Objects.nonNull(this.cacheL1)) {
            this.cacheL1.delete(message.toArray(new CMessage[]{}));
        }
    }

    private <T> T wrapRead(final CMessage message) {
        if (Objects.isNull(this.cacheL1)) {
            return null;
        } else {
            return this.cacheL1.read(message);
        }
    }

    private <T> Future<T> wrapReadAsync(final CMessage message) {
        if (Objects.isNull(this.cacheL1)) {
            return Future.succeededFuture();
        } else {
            return this.cacheL1.readAsync(message);
        }
    }

    private Boolean wrapExist(final CMessage message) {
        if (Objects.isNull(this.cacheL1)) {
            return Boolean.FALSE;
        } else {
            return this.cacheL1.exist(message);
        }
    }

    private Future<Boolean> wrapExistAsync(final CMessage message) {
        if (Objects.isNull(this.cacheL1)) {
            return Future.succeededFuture(Boolean.FALSE);
        } else {
            return this.cacheL1.existAsync(message);
        }
    }

    // ------------------ Usage interface -------------------------
    /*
     * -- delete
     *    deleteAsync
     * Delete cache information
     */
    <T> T delete(final List<CMessage> messages, final ErrorSupplier<T> actualSupplier) {
        /* Actual Supplier */
        final Supplier<T> wrapActual = () -> Fn.monad(actualSupplier, null);

        /* After Callback */
        return CacheAside.after(wrapActual, ret -> this.deleteCache(messages));
    }

    <T> Future<T> deleteAsync(final List<CMessage> messages, final ErrorSupplier<Future<T>> actualSupplier) {
        /* Actual Supplier */
        final Supplier<Future<T>> wrapActual = () -> Fn.monadAsync(actualSupplier, null);

        /* After Callback */
        return CacheAside.afterAsync(wrapActual, ret -> this.deleteCache(messages));
    }

    /*
     * -- read
     *    readAsync
     * Read information such as T & List<T> returned from cache here
     */
    <T> T read(final CMessage message, final ErrorSupplier<T> actualSupplier) {
        /* Actual Supplier */
        final Supplier<T> wrapActual = () -> Fn.monad(actualSupplier, null);

        /* Cache Supplier */
        final Supplier<T> wrapCache = () -> this.wrapRead(message);

        /* Read with callback */
        return CacheAside.before(wrapCache, wrapActual, entity -> this.writeCache(message.data(entity)));
    }

    <T> Future<T> readAsync(final CMessage message, final ErrorSupplier<Future<T>> actualSupplier) {
        /* Actual Supplier */
        final Supplier<Future<T>> wrapActual = () -> Fn.monadAsync(actualSupplier, null);

        /* Cache Supplier */
        final Supplier<Future<T>> wrapCache = () -> this.wrapReadAsync(message);

        /* Read with callback */
        return CacheAside.beforeAsync(wrapCache, wrapActual, entity -> this.writeCache(message.data(entity)));
    }

    /*
     *
     * -- exist
     *    existAsync
     * Exist such as T & List<T>, the different point is returned type is Boolean
     */
    Boolean exist(final CMessage message, final ErrorSupplier<Boolean> actualSupplier) {
        /* Actual Supplier */
        final Supplier<Boolean> wrapActual = () -> Fn.monad(actualSupplier, Boolean.FALSE);

        /* Cache Supplier */
        final Supplier<Boolean> wrapCache = () -> this.wrapExist(message);

        /* Read with callback */
        return CacheAside.check(wrapCache, wrapActual);
    }

    Future<Boolean> existAsync(final CMessage message, final ErrorSupplier<Future<Boolean>> actualSupplier) {
        /* Build to supplier */
        final Supplier<Future<Boolean>> wrapActual = () -> Fn.monadAsync(actualSupplier, Boolean.FALSE);

        /* Cache Supplier */
        final Supplier<Future<Boolean>> wrapCache = () -> this.wrapExistAsync(message);

        return CacheAside.checkAsync(wrapCache, wrapActual);
    }
}
