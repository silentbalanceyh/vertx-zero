package io.vertx.mod.crud.init;

import io.macrocosm.specification.app.HAmbient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.atom.IxConfig;
import io.vertx.mod.crud.cv.IxFolder;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.mod.ke.cv.KeMsg;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import static io.vertx.mod.crud.refine.Ix.LOG;

/*
 * Configuration class initialization
 * plugin/crud/configuration.json
 *
 */
class IxConfiguration {

    /* Module Registry */
    private static final Set<String> MODULE_REG =
        new HashSet<>();
    private static IxConfig CONFIG = null;

    static void registry(final HAmbient ambient) {
        /*
         * Read definition of security configuration from RBAC default folder
         */
        if (null == CONFIG) {
            final JsonObject configData = Ut.ioJObject(IxFolder.CONFIG_FILE);
            final String module = Ke.getExtension(KeIpc.Module.CRUD);
            LOG.Init.info(IxConfiguration.class, KeMsg.Configuration.DATA_J,
                module, configData.encode());

            ambient.registry(module, configData);

            CONFIG = Ut.deserialize(configData, IxConfig.class);
            LOG.Init.info(IxConfiguration.class, KeMsg.Configuration.DATA_T,
                CONFIG.toString());
        }
    }

    static void addUrs(final String key) {
        final JsonArray patterns = CONFIG.getPatterns();
        patterns.stream()
            .map(item -> (String) item)
            .map(pattern -> MessageFormat.format(pattern, key))
            .forEach(MODULE_REG::add);
    }

    static Set<String> getUris() {
        return MODULE_REG;
    }

    static String getField() {
        return CONFIG.getColumnKeyField();
    }

    static String getLabel() {
        return CONFIG.getColumnLabelField();
    }
}
