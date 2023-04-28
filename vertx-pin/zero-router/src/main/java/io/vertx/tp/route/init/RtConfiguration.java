package io.vertx.tp.route.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.route.atom.RtConfig;
import io.vertx.tp.route.cv.RtFolder;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import static io.vertx.tp.route.refine.Rt.LOG;

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
