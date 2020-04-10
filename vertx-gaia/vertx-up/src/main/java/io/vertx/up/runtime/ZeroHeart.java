package io.vertx.up.runtime;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.DynamicVisitor;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

public class ZeroHeart {

    private static final Annal LOGGER = Annal.get(ZeroHeart.class);
    private static final String INIT = "init";
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    public static void init() {
        // inject configuration
        final JsonObject config = VISITOR.read();
        /*
         * Check whether there exist `init` node for class
         * Each `init` clazz must be configured as
         * init:
         * - clazz: XXXX
         *   config:
         *      key1:value1
         *      key2:value2
         */
        if (config.containsKey(INIT)) {
            final JsonArray initArray = config.getJsonArray(INIT);
            /* Component Init */
            Ut.itJArray(initArray, (init, index) -> Ux.initComponent(init));
        }
    }

    /*
     * Shared Map
     */
    public static boolean isShared() {
        final JsonObject options = VISITOR.read();
        return options.containsKey(Plugins.Infix.SHARED);
    }

    /*
     * Session
     */
    public static boolean isSession() {
        final JsonObject options = VISITOR.read();
        return options.containsKey(Plugins.Infix.SESSION);
    }

    /*
     * Micro
     */
    public static boolean isEtcd() {
        final JsonObject options = VISITOR.read();
        return options.containsKey("etcd");
    }

    /*
     * Whether Current node is ApiGateway
     */
    public static boolean isGateway() {
        /*
         * This method is only ok when `micro` mode
         * Secondary Scanned for Api Gateway
         */
        final Set<Integer> apiScanned = new HashSet<>();
        Fn.outUp(() -> {
            final ServerVisitor<HttpServerOptions> visitor =
                    Ut.singleton(DynamicVisitor.class);
            apiScanned.addAll(visitor.visit(ServerType.API.toString()).keySet());
        }, LOGGER);
        return !apiScanned.isEmpty();
    }
}
