package io.vertx.up.uca.cache;

import java.util.Collection;
import java.util.Map;

/**
 * Cache Data: it will store the following part here
 * - CdMemory: ConcurrentMap storage in default
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Cd<K, V> {

    void data(K key, V value);

    V data(K key);

    <M extends Map<K, V>> M data();

    void clear();

    boolean is(K key);

    Collection<V> values();
}
