package io.vertx.up.extension.dot;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.extension.PlugAuditor;
import io.vertx.up.util.Ut;

class PluginAuditor {
    /*
     * Infusion for Auditor
     */
    private static final Cc<String, PlugAuditor> CC_PLUGIN = Cc.openThread();

    static Future<Envelop> audit(final RoutingContext context, final Envelop envelop) {
        return Plugin.mountPlugin(YmlCore.extension.AUDITOR, envelop, (auditCls, config) -> {
            /*
             * Extend PlugAuditor for auditing system setting for some spec business.
             */
            final PlugAuditor auditor = CC_PLUGIN.pick(() -> Ut.instance(auditCls), auditCls.getName());
            // Fn.po?lThread(POOL_PLUGIN, () -> Ut.instance(auditCls), auditCls.getName());
            return auditor.bind(config).audit(context, envelop);
        });
    }
}
