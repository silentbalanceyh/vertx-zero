package io.vertx.up.uca.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Rapid<K, V> {
    /*
     * Pool:
     *    k = t
     */
    static <TK, TV> Rapid<TK, TV> t(final String key) {
        return t(key, -1);
    }

    static <TK, TV> Rapid<TK, TV> t(final String key, final int ttl) {
        return Fn.poolThread(P.CACHED_THREAD, () -> new RapidT<TV>(key, ttl),
            RapidT.class.getName() + key + ttl);
    }

    /*
     * Pool:
     *    k1 = t1
     *    k2 = t2
     */
    static Rapid<Set<String>, ConcurrentMap<String, JsonArray>> map(final String key) {
        return map(key, -1);
    }

    static Rapid<Set<String>, ConcurrentMap<String, JsonArray>> map(final String key, final int ttl) {
        return Fn.poolThread(P.CACHED_THREAD, () -> new RapidMap(key, ttl),
            RapidMap.class.getName() + key + ttl);
    }

    default Future<V> cached(final K key, final Supplier<Future<V>> executor) {
        throw new _501NotSupportException(getClass());
    }

    default Future<V> cached(final K key, final Function<K, Future<V>> executor) {
        throw new _501NotSupportException(getClass());
    }

    default Future<V> write(K key, V value) {
        return write(key, value, -1);
    }

    Future<V> write(K key, V value, int expired);
}

@SuppressWarnings("all")
interface P {
    ConcurrentMap<String, Rapid> CACHED_THREAD = new ConcurrentHashMap<>();
}