package io.vertx.tp.atom.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.AoFolder;
import io.vertx.tp.atom.modeling.config.AoConfig;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.tp.atom.refine.Ao.LOG;

class AoConfiguration {

    private static AoConfig CONFIG = null;

    static void init() {
        /*
         * Read definition of ambient configuration of default
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(AoFolder.CONFIG_FILE);
            LOG.Init.info(AoConfiguration.class, "Ao Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, AoConfig.class);
            LOG.Init.info(AoConfiguration.class, "Ao Configuration: {0}", CONFIG.toString());
        }
    }

    static AoConfig getConfig() {
        // Fix Issue of Atom Disabled Null Pointer
        return Objects.isNull(CONFIG) ? new AoConfig() : CONFIG;
    }
}
