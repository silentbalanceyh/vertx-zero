package io.vertx.up.experiment.mixture;

import io.vertx.core.json.JsonObject;
import io.vertx.up.extension.pointer.PluginExtension;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadSmart implements HLoad {
    private static final Cc<String, HLoad> CC_NORM = Cc.openThread();
    private final transient JsonObject config = new JsonObject();

    private final transient HLoad loader;

    public HLoadSmart() {
        this.loader = CC_NORM.pick(HLoadNorm::new, HLoadNorm.class.getName());
    }

    @Override
    public HLoad bind(final JsonObject config) {
        this.config.mergeIn(config);
        return this;
    }

    @Override
    public HAtom atom(final String appName, final String identifier) {
        final Boolean after = this.config.getBoolean("after", Boolean.TRUE);
        /*
         * Load Sequence calculation
         *
         * 1. after = true
         *    HLoadNorm -> extension loader
         * 2. after = false
         *    extension loader -> HLoadNorm
         */
        HAtom atom;
        if (after) {
            // Default Situation
            // Static
            atom = this.loader.atom(appName, identifier);
            if (Objects.isNull(atom)) {
                // Dynamic
                atom = PluginExtension.Loader.atom(appName, identifier);
            }
        } else {
            // Dynamic
            atom = PluginExtension.Loader.atom(appName, identifier);
            if (Objects.isNull(atom)) {
                // Static
                atom = this.loader.atom(appName, identifier);
            }
        }
        return atom;
    }
}
