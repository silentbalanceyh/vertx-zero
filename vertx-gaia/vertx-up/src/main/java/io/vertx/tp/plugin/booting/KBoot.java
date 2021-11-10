package io.vertx.tp.plugin.booting;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
        if (Pool.BOOTS.isEmpty()) {
            final Node<JsonObject> node = Ut.singleton(ZeroUniform.class);
            final JsonArray boots = node.read().getJsonArray("boot", new JsonArray());
            Ut.itJArray(boots).forEach(json -> {
                final Class<?> bootCls = Ut.clazz(json.getString("executor"), null);
                if (Objects.nonNull(bootCls)) {
                    Fn.pool(Pool.BOOTS, bootCls, () -> Ut.instance(bootCls));
                }
            });
        }
        return new HashSet<>(Pool.BOOTS.values());
    }

    /*
     *  Following two methods are for data loading
     */
    ConcurrentMap<String, KConnect> configure();

    List<String> oob();

    List<String> oob(String prefix);

    /*
     * Following two methods are for Crud Default Value
     */
    ConcurrentMap<String, JsonObject> module();

    ConcurrentMap<String, JsonArray> column();
}
