package io.vertx.tp.battery.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.atom.PowerConfig;
import io.vertx.tp.battery.cv.BkCv;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.vertx.tp.battery.refine.Bk.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BkConfiguration {
    private static final Node<JsonObject> READER = Ut.singleton(ZeroUniform.class);
    private static PowerConfig CONFIG;

    private BkConfiguration() {
    }

    static void init() {
        final JsonObject configJson = READER.read();
        if (configJson.containsKey(BkCv.FOLDER_MODULE)) {
            final JsonObject configuration = configJson.getJsonObject(BkCv.FOLDER_MODULE, new JsonObject());
            LOG.Init.info(BkConfiguration.class, "The Modulat Engine will be initialized!! `{0}`",
                configuration.encode());
            CONFIG = Ut.deserialize(configuration, PowerConfig.class);
        }
    }

    static Set<String> builtIn() {
        if (Objects.isNull(CONFIG)) {
            init();
        }
        return Objects.isNull(CONFIG) ? new HashSet<>() : CONFIG.buildIn();
    }
}
