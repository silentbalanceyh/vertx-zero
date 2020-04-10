package io.vertx.tp.jet.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

/*
 * Configuration of zero here
 * This extension configuration is different from other extension
 * The json config must be set in `vertx-jet.yml` or other tp extension
 */
class JtConfiguration {
    private static final String KEY_ROUTER = "router";
    private static final Annal LOGGER = Annal.get(JtConfiguration.class);
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    private static JtConfig CONFIG = null;

    static void init() {
        if (null == CONFIG) {
            final JsonObject configData = VISITOR.read();
            if (configData.containsKey(KEY_ROUTER)) {
                final JsonObject router = configData.getJsonObject(KEY_ROUTER);
                Jt.infoInit(LOGGER, "Jt Router Json: {0}", router.encode());
                CONFIG = Ut.deserialize(router, JtConfig.class);
                Jt.infoInit(LOGGER, "Jt Configuration: {0}", CONFIG.toString());
                Jt.infoInit(LOGGER, "---> Jt @Wall for `{0}`", CONFIG.getWall());
            }
        }
    }

    static JtConfig getConfig() {
        return CONFIG;
    }
}
