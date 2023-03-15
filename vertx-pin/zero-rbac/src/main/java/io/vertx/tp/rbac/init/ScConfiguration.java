package io.vertx.tp.rbac.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.ScFolder;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Configuration class initialization
 * plugin/rbac/configuration.json
 *
 */
class ScConfiguration {
    private static final Annal LOGGER = Annal.get(ScConfiguration.class);
    private static ScConfig CONFIG = null;

    static void init() {
        /*
         * Read definition of security configuration from RBAC default folder
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(ScFolder.CONFIG_FILE);
            Sc.infoInit(LOGGER, "Sc Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, ScConfig.class);
            Sc.infoInit(LOGGER, "Sc Configuration: {0}", CONFIG.toString());
        }
    }

    static ScConfig getConfig() {
        if (Objects.isNull(CONFIG)) {
            init();
        }
        return CONFIG;
    }
}
