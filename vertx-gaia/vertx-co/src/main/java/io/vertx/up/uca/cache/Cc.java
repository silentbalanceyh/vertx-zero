package io.vertx.up.uca.cache;

import io.vertx.up.eon.em.CcMode;
import io.vertx.up.exception.web._501NotSupportException;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Cache Interface for three usage:
 *
 * - Component Cache
 * - Config Cache
 * - Thread Component Cache
 *
 * It's new structure for different cache stored instead of single one
 *
 * 1) Here are Cd data structure to control internal storage.
 * 2) The default implementation class is `CdMem` ( Default Cd )
 *
 * Cc: Component Cache / Config Cache
 * Cd: Component Data / Cache Data
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Cc<K, V> {

    static <K, V> Cc<K, V> open(final CcMode mode) {
        /*
         * Here are three default implementation of Cc, create new Cc reference and
         * here are all situations for different usage of cache
         * 1) When the Cc created, it is often called in STATIC context
         * 2) Each Cc has three method for:
         * -  pick(Supplier)            -> Singleton or Thread Singleton
         * -  pick(Supplier, K)         -> Pooled by K
         * -  pick(Supplier, Class<?>)  -> Pooled by Class<?> ( named often )
         *
         * Please do not call `Cc.open(CcMode)` in NON-STATIC context because of that
         * it create new Cc in each time and here is no cache of Cc it-self
         *
         * This data structure should replace all the `ConcurrentMap` data structure in:
         * - Zero Framework
         * - Zero Extension Framework
         * - Zero Based Application
         */
        if (Objects.isNull(mode)) {
            throw new _501NotSupportException(Cc.class);
        }
        return null;
    }

    Cd<K, V> data();

    V pick(Supplier<V> supplier);

    V pick(Supplier<V> supplier, Class<?> key);

    V pick(Supplier<V> supplier, K key);
}
