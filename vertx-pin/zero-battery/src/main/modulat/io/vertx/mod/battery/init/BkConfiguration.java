package io.vertx.mod.battery.init;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.battery.atom.PowerConfig;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.vertx.mod.battery.refine.Bk.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BkConfiguration {
    private static PowerConfig CONFIG;

    private BkConfiguration() {
    }

    static void init() {
        if (ZeroStore.is(YmlCore.module.__KEY)) {
            final JsonObject configuration = ZeroStore.option(YmlCore.module.__KEY);
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
