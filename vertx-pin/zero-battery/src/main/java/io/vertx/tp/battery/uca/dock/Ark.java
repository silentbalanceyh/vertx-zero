package io.vertx.tp.battery.uca.dock;

import io.vertx.aeon.eon.em.TypeBy;
import io.vertx.core.Future;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.uca.cache.Cc;

/*
 * Connect to HArk part for configuration in each application
 * This interface may call HArk in future with adapter.
 */
public interface Ark {
    Cc<String, Ark> CC_ARK = Cc.openThread();

    static Ark configure() {
        return CC_ARK.pick(ArkConfigure::new, ArkConfigure.class.getName());
    }

    static Ark bag() {
        return CC_ARK.pick(ArkBag::new, ArkBag.class.getName());
    }

    /*
     * Fetch data from the system by `appId`
     * instead of other modulat.
     */
    default Future<ClusterSerializable> modularize(final String appId) {
        return this.modularize(appId, TypeBy.BY_ID);
    }

    Future<ClusterSerializable> modularize(String appId, TypeBy by);
}
