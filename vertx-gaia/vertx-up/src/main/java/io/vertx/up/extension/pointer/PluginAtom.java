package io.vertx.up.extension.pointer;

import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class PluginAtom {
    /*
     * Plugin for Auditor
     */
    private static final String ATOM = "atom";
    private static final Cc<String, HLoad> CC_PLUGIN = Cc.openThread();

    static HAtom atom(final String namespace, final String identifier) {
        return Plugin.mountPlugin(ATOM, (atomCls, config) -> {
            final HLoad loader = CC_PLUGIN.pick(() -> Ut.instance(atomCls));
            /*
             * Bind configuration of
             * atom:
             *   component:
             *   config
             */
            return loader.bind(config.copy()).atom(namespace, identifier);
        }, () -> null);
    }
}
