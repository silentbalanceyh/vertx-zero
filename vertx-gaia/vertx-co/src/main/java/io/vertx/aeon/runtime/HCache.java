package io.vertx.aeon.runtime;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.aeon.specification.app.HFS;
import io.vertx.up.uca.cache.Cc;

/**
 * 「运行时组件缓存」
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCache {
    // Sync
    // -- hashCode
    Cc<Integer, HAeon> CC_AEON = Cc.open();

    Cc<Integer, HBoot> CC_BOOT = Cc.open();
    // -- thread
    @SuppressWarnings("all")
    Cc<String, HEvent> CCT_EVENT = Cc.openThread();

    Cc<String, HFS> CCT_FS = Cc.openThread();
}
