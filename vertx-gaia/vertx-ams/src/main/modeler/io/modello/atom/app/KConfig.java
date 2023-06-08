package io.modello.atom.app;

import io.horizon.util.HUt;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.json.JsonObject;

/**
 * 「常用配置」
 * 读取配置专用，常用配置可以用来直接生成 {@link io.macrocosm.atom.boot.KSetting}
 *
 * @author lang : 2023-05-30
 */
public class KConfig implements HConfig {

    private final JsonObject options = new JsonObject();

    @Override
    public JsonObject options() {
        return this.options;
    }

    @Override
    public HConfig options(final JsonObject options) {
        this.options.mergeIn(HUt.valueJObject(options));
        return this;
    }
}
