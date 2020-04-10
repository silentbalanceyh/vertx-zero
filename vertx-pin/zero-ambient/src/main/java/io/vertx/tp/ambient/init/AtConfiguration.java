package io.vertx.tp.ambient.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtFolder;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/*
 * Configuration class initialization
 * plugin/ambient/configuration.json
 */
class AtConfiguration {
    /*
     * Logger for IxDao
     */
    private static final Annal LOGGER = Annal.get(AtConfiguration.class);
    private static AtConfig CONFIG = null;

    static void init() {
        /*
         * Read definition of ambient configuration of default
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(AtFolder.CONFIG_FILE);
            At.infoInit(LOGGER, "At Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, AtConfig.class);
            At.infoInit(LOGGER, "At Configuration: {0}", CONFIG.toString());
        }
    }

    static AtConfig getConfig() {
        return CONFIG;
    }
}
