package io.vertx.tp.plugin.cache.hit;

import io.vertx.tp.plugin.cache.l1.L1Kit;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HKId implements HKey {
    private transient String id;

    /*
     * VertxDao processing, class mapped to L1Key
     */
    public HKId(final Object id) {
        if (Objects.nonNull(id)) {
            this.id = id.toString();
        }
    }

    @Override
    public String unique(final HMeta meta) {
        final Class<?> entityCls = meta.type();
        final TreeMap<String, String> keyMap = new TreeMap<>();
        keyMap.put("key", this.id);
        return L1Kit.keyString(entityCls.getName(), keyMap);
    }
}
