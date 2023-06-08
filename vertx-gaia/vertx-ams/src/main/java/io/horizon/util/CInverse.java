package io.horizon.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author lang : 2023/4/30
 */
class CInverse {
    /*
     * Complex calculation for
     * k1 = v1,
     * k2 = v2,
     * k3 = v1,
     * ....
     *
     * convert to
     * v1 = (k1, k3)
     * v2 = (k2)
     *
     * The function could convert
     * (k1, k3) -> vv1
     * (k2) -> vv2
     */
    static <K, V, R> ConcurrentMap<V, R> inverse(final ConcurrentMap<K, V> input, final Function<Set<K>, R> function) {
        final ConcurrentMap<V, R> inverseMap = new ConcurrentHashMap<>();
        if (Objects.nonNull(input) && !input.isEmpty()) {
            final ConcurrentMap<V, Set<K>> valueMap = new ConcurrentHashMap<>();
            input.forEach((k, v) -> {
                if (Objects.nonNull(k) && Objects.nonNull(v)) {
                    final Set<K> kSet;
                    if (valueMap.containsKey(v)) {
                        kSet = valueMap.get(v);
                    } else {
                        kSet = new HashSet<>();
                        valueMap.put(v, kSet);
                    }
                    kSet.add(k);
                }
            });
            valueMap.forEach((v, kSet) -> {
                final R r = function.apply(kSet);
                if (Objects.nonNull(r)) {
                    inverseMap.put(v, r);
                }
            });
        }
        return inverseMap;
    }
}
