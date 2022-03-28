package io.vertx.tp.is.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.atom.IsConfig;
import io.vertx.tp.is.cv.IsFolder;
import io.vertx.tp.is.refine.Is;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class IsConfiguration {
    private static IsConfig CONFIG = null;

    static void init() {
        if (Objects.isNull(CONFIG)) {
            final JsonObject configData = Ut.ioJObject(IsFolder.CONFIG_FILE);
            Is.Log.infoInit(IsConfiguration.class, "Is Json Data: {0}", configData.encode());
            CONFIG = Ut.deserialize(configData, IsConfig.class);
            Is.Log.infoInit(IsConfiguration.class, "Is Configuration: {0}", CONFIG.toString());
        }
    }

    static IsConfig getConfig() {
        return CONFIG;
    }
}
