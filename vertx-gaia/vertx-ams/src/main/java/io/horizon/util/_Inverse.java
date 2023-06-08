package io.horizon.util;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/30
 */
class _Inverse extends _From {
    protected _Inverse() {
    }

    /**
     * （尺寸逆向）哈希表的逆向操作，直接将一个哈希表做逆向的聚集计算，生成逆向哈希表
     *
     * @param input 输入哈希表
     * @param <K>   输入哈希表的键类型
     * @param <V>   输入哈希表的值类型
     *
     * @return 逆向哈希表
     */
    public static <K, V> ConcurrentMap<V, Integer> inverseCount(final ConcurrentMap<K, V> input) {
        return CInverse.inverse(input, Set::size);
    }

    /**
     * （标准逆向）哈希表的逆向操作，直接将一个哈希表做逆向的聚集计算，生成逆向哈希表
     *
     * @param input 输入哈希表
     * @param <K>   输入哈希表的键类型
     * @param <V>   输入哈希表的值类型
     *
     * @return 逆向哈希表
     */
    public static <K, V> ConcurrentMap<V, Set<K>> inverseSet(final ConcurrentMap<K, V> input) {
        return CInverse.inverse(input, set -> set);
    }
}
