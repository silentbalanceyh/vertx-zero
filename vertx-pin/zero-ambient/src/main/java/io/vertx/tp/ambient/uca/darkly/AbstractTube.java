package io.vertx.tp.ambient.uca.darkly;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.experiment.mixture.HLoadSmart;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTube implements Tube {
    private static final Cc<String, HLoad> CC_SMART = Cc.openThread();

    protected HAtom atom(final XActivityRule rule) {
        // Double-Checking for `rule`
        if (Objects.isNull(rule)) {
            return null;
        }
        if (Objects.isNull(rule.getRuleIdentifier())) {
            return null;
        }
        final HLoad loader = CC_SMART.pick(HLoadSmart::new, HLoadSmart.class.getName());
        /*
         * Ns may be `null`
         */
        return loader.atom(rule.getRuleNs(), rule.getRuleIdentifier());
    }
}
