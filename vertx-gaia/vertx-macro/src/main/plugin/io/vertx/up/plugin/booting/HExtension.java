package io.vertx.up.plugin.booting;


import io.horizon.eon.VSpec;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    Cc<Class<?>, HExtension> CC_BOOTS = Cc.open();
}

/**
 * Data Booting for configuration
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HExtension {
    /*
     * Capture all HExtension components
     */
    static Set<HExtension> initialize() {
        /* Boot processing */
        final ConcurrentMap<Class<?>, HExtension> data = Pool.CC_BOOTS.store();
        if (data.isEmpty()) {
            final JsonObject launcher = ZeroStore.launcherJ();
            final JsonArray boots = Ut.valueJArray(launcher, VSpec.Boot.EXTENSION);
            Ut.itJArray(boots).forEach(json -> {
                final Class<?> bootCls = Ut.clazz(json.getString("executor"), null);
                if (Objects.nonNull(bootCls)) {
                    Pool.CC_BOOTS.pick(() -> Ut.instance(bootCls), bootCls);
                    // Fn.po?l(Pool.BOOTS, bootCls, () -> Ut.instance(bootCls));
                }
            });
        }
        return new HashSet<>(data.values());
    }

    /*
     *  Following two methods are for data loading
     *  - First one is used by Excel Loader  ( zero-ifx-excel )
     *  - The second has been used by BtBoot ( zero-ke )
     */
    ConcurrentMap<String, KConnect> configure();

    List<String> oob();

    List<String> oob(String prefix);

    /*
     * Following two methods are for Crud Default Value
     *  - First has been used by CRUD Extension ( zero-crud )
     *  - Second has been used by UI Extension  ( zero-ui )
     * They are not for data loading but for runtime usage
     */
    ConcurrentMap<String, JsonObject> module();

    ConcurrentMap<String, JsonArray> column();
}
