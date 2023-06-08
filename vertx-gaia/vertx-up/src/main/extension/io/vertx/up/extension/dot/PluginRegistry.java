package io.vertx.up.extension.dot;

import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.extension.PlugRegistry;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

class PluginRegistry {
    /*
     * Infusion for etcd
     */

    static void registry(final Set<String> routes) {
        Plugin.mountPlugin(YmlCore.extension.ETCD, (etcdCls, config) -> {
            /*
             * Extend PlugRegistry for etcd api gateway branch
             * When duplicated uri found in different module, we must parse
             * the module definition and generate new routes
             *
             * The new routes will not be registry to Vert.x, but it should be
             * write to etcd ( Configuration ), then the api could identify
             * the unique uri when get restful service.
             */
            final PlugRegistry registry = Ut.singleton(etcdCls);
            final Set<String> input = new HashSet<>(routes);
            final Set<String> processed = registry.analyze(input);
            /*
             * Clear original routes and add all processed
             * Once you enabled this plug-in, it means that you have route
             * calculated method that you defined.
             */
            routes.clear();
            routes.addAll(processed);
        });
    }
}
