package io.vertx.tp.battery.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.battery.atom.MetaBattery;
import io.vertx.tp.battery.cv.BkCv;
import io.vertx.tp.battery.refine.Bk;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BkConfiguration {
    private static final Node<JsonObject> READER = Ut.singleton(ZeroUniform.class);
    private static MetaBattery CONFIG;

    private BkConfiguration() {
    }

    static void init() {
        final JsonObject configJson = READER.read();
        if (configJson.containsKey(BkCv.FOLDER_MODULE)) {
            final JsonObject configuration = configJson.getJsonObject(BkCv.FOLDER_MODULE, new JsonObject());
            Bk.Log.infoInit(BkConfiguration.class, "The Modulat Engine will be initialized!! `{0}`",
                configuration.encode());
            CONFIG = Ut.deserialize(configuration, MetaBattery.class);
        }
    }

    static Set<String> builtIn() {
        if (Objects.isNull(CONFIG)) {
            init();
        }
        return Objects.isNull(CONFIG) ? new HashSet<>() : CONFIG.buildIn();
    }
}
