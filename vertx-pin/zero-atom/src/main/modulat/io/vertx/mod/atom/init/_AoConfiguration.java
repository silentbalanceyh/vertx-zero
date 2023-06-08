package io.vertx.mod.atom.init;

import io.macrocosm.specification.app.HAmbient;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.AoFolder;
import io.vertx.mod.atom.modeling.config.AoConfig;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.mod.ke.cv.KeMsg;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.mod.atom.refine.Ao.LOG;

class AoConfiguration {

    private static AoConfig CONFIG = null;

    static void registry(final HAmbient ambient) {
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(AoFolder.CONFIG_FILE);
            final String module = Ke.getExtension(KeIpc.Module.ATOM);
            LOG.Init.info(AoConfiguration.class, KeMsg.Configuration.DATA_J,
                module, configData.encode());

            ambient.registry(module, configData);

            CONFIG = Ut.deserialize(configData, AoConfig.class);
            LOG.Init.info(AoConfiguration.class, KeMsg.Configuration.DATA_T,
                CONFIG.toString());
        }
    }

    static AoConfig getConfig() {
        // Fix Issue of Atom Disabled Null Pointer
        return Objects.isNull(CONFIG) ? new AoConfig() : CONFIG;
    }
}
