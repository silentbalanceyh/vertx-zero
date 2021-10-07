package io.vertx.up.extension.pointer;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.PlugAuditor;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class PluginAuditor {
    /*
     * Plugin for Auditor
     */
    private static final String AUDITOR = "auditor";
    private static final ConcurrentMap<String, PlugAuditor> POOL_PLUGIN = new ConcurrentHashMap<>();

    static Future<Envelop> audit(final RoutingContext context, final Envelop envelop) {
        return Plugin.mountPlugin(AUDITOR, envelop, (auditCls, config) -> {
            /*
             * Extend PlugAuditor for auditing system setting for some spec business.
             */
            final PlugAuditor auditor = Fn.poolThread(POOL_PLUGIN, () -> Ut.instance(auditCls), auditCls.getName());
            return auditor.bind(config).audit(context, envelop);
        });
    }
}
