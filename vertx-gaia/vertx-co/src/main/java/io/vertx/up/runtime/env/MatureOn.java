package io.vertx.up.runtime.env;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.record.AttrSet;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

/**
 * 专用环境变量统一入口，可直接提取核心环境变量，根据不同维度对环境变量进行分类
 * immutable on
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MatureOn implements Macrocosm {
    private static final Cc<String, Mature> CC_MATURE = Cc.openThread();
    // Cloud Connected

    // Development Connected
    public static JsonObject envPlot(final JsonObject plot) {
        final AttrSet set = AttrSet.of()
                .save(KName.CLOUD, AEON_CLOUD)                                  // AEON_CLOUD
                .save(KName.CHILD, AEON_APP)                                    // AEON_APP
                .save(KName.NAME, Z_APP)                                        // Z_APP
                .save(KName.NAMESPACE, Z_NS)                                    // Z_NS
                .save(KName.LANGUAGE, Z_LANG, Constants.DEFAULT_LANGUAGE)       // Z_LANG
                .save(KName.SIGMA, Z_SIGMA);                                    // Z_SIGMA
        final JsonObject plotJ = Ut.valueJObject(plot);
        final Mature mature = CC_MATURE.pick(MatureEnv::new, MatureEnv.class.getName());
        return mature.configure(plotJ, set);
    }
}
