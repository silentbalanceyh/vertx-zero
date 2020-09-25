package io.vertx.tp.plugin.cache.hit;

import io.vertx.tp.plugin.cache.util.CacheTool;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CacheId implements CacheKey {
    private transient String id;

    /*
     * VertxDao processing, class mapped to L1Key
     */
    public CacheId(final Object id) {
        if (Objects.nonNull(id)) {
            this.id = id.toString();
        }
    }

    @Override
    public String unique(final CacheMeta meta) {
        final Class<?> entityCls = meta.type();
        final TreeMap<String, String> keyMap = new TreeMap<>();
        keyMap.put("key", this.id);
        return CacheTool.dataKey(entityCls.getName(), keyMap);
    }
}
