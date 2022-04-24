package io.vertx.tp.plugin.booting;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    Cc<Class<?>, KBoot> CC_BOOTS = Cc.open();
}

/**
 * Data Booting for configuration
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface KBoot {
    /*
     * Capture all KBoot components
     */
    static Set<KBoot> initialize() {
        /* Boot processing */
        final ConcurrentMap<Class<?>, KBoot> data = Pool.CC_BOOTS.store().data();
        if (data.isEmpty()) {
            final Node<JsonObject> node = Ut.singleton(ZeroUniform.class);
            final JsonArray boots = node.read().getJsonArray("boot", new JsonArray());
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
