package io.vertx.tp.atom.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.AoFolder;
import io.vertx.tp.atom.modeling.config.AoConfig;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.util.Ut;

class AoConfiguration {

    private static AoConfig CONFIG = null;

    static void init() {
        /*
         * Read definition of ambient configuration of default
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(AoFolder.CONFIG_FILE);
            Ao.infoInit(AoConfiguration.class, "Ao Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, AoConfig.class);
            Ao.infoInit(AoConfiguration.class, "Ao Configuration: {0}", CONFIG.toString());
        }
    }

    static AoConfig getConfig() {
        return CONFIG;
    }
}
