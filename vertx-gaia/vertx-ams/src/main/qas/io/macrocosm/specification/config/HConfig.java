package io.macrocosm.specification.config;

import io.modello.atom.app.KConfig;
import io.vertx.core.json.JsonObject;

/**
 * 「配置接口」
 * 针对组件的核心配置接口，重新设计配置层
 *
 * @author lang : 2023-05-30
 */
public interface HConfig {
    /**
     * 默认配置
     *
     * @param options 配置信息
     *
     * @return {@link io.macrocosm.specification.config.HConfig}
     */
    static HConfig of(final JsonObject options) {
        return new KConfig().options(options);
    }

    default JsonObject options() {
        return new JsonObject();
    }

    default HConfig options(final JsonObject options) {
        return this;
    }
}
