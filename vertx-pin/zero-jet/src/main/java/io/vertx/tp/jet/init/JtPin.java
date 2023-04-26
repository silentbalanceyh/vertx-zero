package io.vertx.tp.jet.init;

import io.horizon.spi.environment.Ambient;
import io.horizon.spi.environment.UnityApp;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.tp.jet.refine.Jt.LOG;

/*
 * Configuration of zero here
 * This extension configuration is different from other extension
 * The json config must be set in `vertx-jet.yml` or other tp extension
 */
public class JtPin {
    private static final String KEY_ROUTER = "router";
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);
    private static final Annal LOGGER = Annal.get(JtPin.class);
    private static JtConfig CONFIG = null;

    static void initializing() {
        if (null == CONFIG) {
            final JsonObject configData = VISITOR.read();
            if (configData.containsKey(KEY_ROUTER)) {
                final JsonObject router = configData.getJsonObject(KEY_ROUTER);
                LOG.Init.info(LOGGER, "Jt Router Json: {0}", router.encode());
                CONFIG = Ut.deserialize(router, JtConfig.class);
                LOG.Init.info(LOGGER, "Jt Configuration: {0}", CONFIG.toString());
                LOG.Init.info(LOGGER, "---> Jt @Wall for `{0}`", CONFIG.getWall());
            }
        }
    }

    public static Future<Boolean> init(final Vertx vertx) {
        Ke.banner("「Πίδακας δρομολογητή」- ( Api )");
        LOG.Init.info(LOGGER, "JtConfiguration...");
        initializing();
        LOG.Init.info(LOGGER, "Ambient Environment Start...");
        return Ambient.init(vertx);
    }

    public static JtConfig getConfig() {
        return CONFIG;
    }

    public static UnityApp getUnity() {
        final UnityApp unity = Ut.singleton(CONFIG.getUnity());
        return Objects.isNull(unity) ? null : unity;
    }
}
