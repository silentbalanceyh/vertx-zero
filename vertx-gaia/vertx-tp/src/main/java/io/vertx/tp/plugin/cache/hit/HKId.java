package io.vertx.tp.plugin.cache.hit;

import java.util.Objects;

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
        return this.id;
    }
}
