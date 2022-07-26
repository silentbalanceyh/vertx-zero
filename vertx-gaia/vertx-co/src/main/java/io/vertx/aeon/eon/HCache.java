package io.vertx.aeon.eon;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.specification.boot.HOn;
import io.vertx.aeon.specification.program.HAlive;
import io.vertx.up.uca.cache.Cc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCache {
    // Sync
    // -- hashCode
    Cc<Integer, HAeon> CC_AEON = Cc.open();

    Cc<Integer, HBoot> CC_BOOT = Cc.open();
    // -- thread
    Cc<String, HOn> CCT_ON = Cc.openThread();
    Cc<String, HAlive> CCT_ALIVE = Cc.openThread();
}
