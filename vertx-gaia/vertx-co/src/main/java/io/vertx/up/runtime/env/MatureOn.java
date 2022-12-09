package io.vertx.up.runtime.env;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.record.AttrSet;

/**
 * 专用环境变量统一入口，可直接提取核心环境变量，根据不同维度对环境变量进行分类
 * immutable on
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MatureOn implements Macrocosm {
    // Cloud Connected

    // Development Connected
    public static JsonObject envPlot(final JsonObject plotJ) {
        final AttrSet set = AttrSet.of();
        return null;
    }
}
