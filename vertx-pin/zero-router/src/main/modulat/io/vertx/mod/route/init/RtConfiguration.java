package io.vertx.mod.route.init;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.route.atom.RtConfig;
import io.vertx.mod.route.cv.RtFolder;
import io.vertx.up.util.Ut;

import static io.vertx.mod.route.refine.Rt.LOG;

class RtConfiguration {
    private static final Annal LOGGER = Annal.get(RtConfiguration.class);

    private static RtConfig CONFIG = null;

    static void init() {
        if (null == CONFIG) {
            final JsonObject rtData = Ut.ioJObject(RtFolder.CONFIG_FILE);
            LOG.Init.info(LOGGER, "Rt Json Data: {0}", rtData.encode());
            CONFIG = Ut.deserialize(rtData, RtConfig.class);
            /* Static Loading */
            LOG.Init.info(LOGGER, "Rt Configuration: {0}", CONFIG.toString());
        }
    }

    static RtConfig getConfig() {
        return CONFIG;
    }
}
