package io.vertx.up.plugin.init;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.exception.booting.DynamicKeyMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;

import java.io.Serializable;

/**
 * Third part configuration data.
 * endpoint: Major endpoint
 * config: Configuration Data of third part.
 */
public class TpConfig implements Serializable {

    private static final Annal LOGGER = Annal.get(TpConfig.class);

    private static final Cc<String, TpConfig> CC_CACHE = Cc.open();

    private static final String KEY_ENDPOINT = "endpoint";
    private static final String KEY_CONFIG = "config";

    private final transient JsonObject config;
    private final transient String endpoint;

    public TpConfig(final String key, final String rule) {
        final JsonObject raw = ZeroStore.option(key);
        // Check up exception for key
        Fn.outBoot(!ZeroStore.is(key),
            LOGGER, DynamicKeyMissingException.class,
            this.getClass(), key, raw);

        // Check up exception for JsonObject
        this.endpoint = Fn.runOr(null, () -> raw.getString(KEY_ENDPOINT), raw.getValue(KEY_ENDPOINT));
        this.config = Fn.runOr(new JsonObject(), () -> raw.getJsonObject(KEY_CONFIG), raw.getValue(KEY_CONFIG));
        // Verify the config data.
        if (null != rule) {
            Fn.outBug(() -> Fn.bugAt(() -> Ruler.verify(rule, this.config), this.config), LOGGER);
        }
    }

    public static TpConfig create(final String key) {
        return CC_CACHE.pick(() -> new TpConfig(key, null), key);
        // return Fn.po?l(CACHE, key, () -> new TpConfig(key, null));
    }

    public static TpConfig create(final String key, final String rule) {
        return CC_CACHE.pick(() -> new TpConfig(key, rule), key);
        // return Fn.po?l(CACHE, key, () -> new TpConfig(key, rule));
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public String getEndPoint() {
        return this.endpoint;
    }
}
