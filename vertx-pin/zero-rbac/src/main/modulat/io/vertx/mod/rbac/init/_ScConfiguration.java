package io.vertx.mod.rbac.init;

import io.macrocosm.specification.app.HAmbient;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.mod.ke.cv.KeMsg;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.rbac.atom.ScConfig;
import io.vertx.mod.rbac.cv.ScFolder;
import io.vertx.up.util.Ut;

import java.util.Objects;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/*
 * Configuration class initialization
 * plugin/rbac/configuration.json
 *
 */
class ScConfiguration {
    private static ScConfig CONFIG = null;

    static void registry(final HAmbient ambient) {
        /*
         * Read definition of security configuration from RBAC default folder
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(ScFolder.CONFIG_FILE);
            final String module = Ke.getExtension(KeIpc.Module.RBAC);
            LOG.Init.info(ScConfiguration.class, KeMsg.Configuration.DATA_J,
                module, configData.encode());

            ambient.registry(module, configData);

            CONFIG = Ut.deserialize(configData, ScConfig.class);
            LOG.Init.info(ScConfiguration.class, KeMsg.Configuration.DATA_T,
                CONFIG.toString());
        }
    }

    static ScConfig getConfig() {
        Objects.requireNonNull(CONFIG);
        return CONFIG;
    }
}
