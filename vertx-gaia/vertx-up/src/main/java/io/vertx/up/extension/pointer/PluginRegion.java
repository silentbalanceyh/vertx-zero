package io.vertx.up.extension.pointer;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.PlugRegion;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class PluginRegion {
    /*
     * Plugin for Region
     */
    private static final String REGION = "region";
    private static final ConcurrentMap<String, PlugRegion> POOL_PLUGIN = new ConcurrentHashMap<>();

    static Future<Envelop> before(final RoutingContext context, final Envelop envelop) {
        return Plugin.mountPlugin(REGION, envelop, (auditCls, config) -> {
            /*
             * Data Region Before: Request
             */
            final PlugRegion region = Fn.poolThread(POOL_PLUGIN, () -> Ut.instance(auditCls), auditCls.getName());
            return region.bind(config).before(context, envelop);
        });
    }

    static Future<Envelop> after(final RoutingContext context, final Envelop envelop) {
        return Plugin.mountPlugin(REGION, envelop, (auditCls, config) -> {
            /*
             * Data Region After: Response
             */
            final PlugRegion region = Fn.poolThread(POOL_PLUGIN, () -> Ut.instance(auditCls), auditCls.getName());
            return region.bind(config).after(context, envelop);
        });
    }
}
