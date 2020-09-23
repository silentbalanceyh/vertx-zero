package io.vertx.up.uca.jooq.cache;

import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.cache.l1.L1Cache;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1Async {

    private final transient L1Cache cacheL1;

    public L1Async() {
        this.cacheL1 = Harp.cacheL1();
    }
}
