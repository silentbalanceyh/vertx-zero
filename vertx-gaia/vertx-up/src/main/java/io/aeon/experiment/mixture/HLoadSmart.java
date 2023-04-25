package io.aeon.experiment.mixture;

import io.horizon.specification.modeler.HAtom;
import io.vertx.up.extension.pointer.PluginExtension;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadSmart implements HLoad {
    private static final Cc<String, HLoad> CC_NORM = Cc.openThread();

    private final transient HLoad loader;

    public HLoadSmart() {
        this.loader = CC_NORM.pick(HLoadNorm::new, HLoadNorm.class.getName());
    }

    @Override
    public HAtom atom(final String appName, final String identifier) {
        /*
         * Load Sequence calculation
         *
         * 1. after = true
         *    HLoadNorm -> extension loader
         * 2. after = false
         *    extension loader -> HLoadNorm
         */
        // Default Situation
        // Static
        HAtom atom = this.loader.atom(appName, identifier);
        if (Objects.isNull(atom)) {
            // Dynamic
            atom = PluginExtension.Loader.atom(appName, identifier);
        }
        return atom;
    }
}
