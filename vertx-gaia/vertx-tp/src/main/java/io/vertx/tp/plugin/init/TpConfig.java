package io.vertx.tp.plugin.init;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.exception.zero.DynamicConfigTypeException;
import io.vertx.up.exception.zero.DynamicKeyMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.yaml.Node;

import java.io.Serializable;

/**
 * Third part configuration data.
 * endpoint: Major endpoint
 * config: Configuration Data of third part.
 */
public class TpConfig implements Serializable {

    private static final Annal LOGGER = Annal.get(TpConfig.class);

    private static final Cc<String, TpConfig> CC_CACHE = Cc.open();

    private static final Node<JsonObject> TP = Node.infix("tp");

    private static final String KEY_ENDPOINT = "endpoint";
    private static final String KEY_CONFIG = "config";

    private final transient JsonObject config;
    private final transient String endpoint;

    public TpConfig(final String key, final String rule) {
        final JsonObject config = TP.read();
        // Check up exception for key
        Fn.outUp(null == config || !config.containsKey(key),
            LOGGER, DynamicKeyMissingException.class,
            this.getClass(), key, config);

        // Check up exception for JsonObject
        final Class<?> type = config.getValue(key).getClass();
        Fn.outUp(JsonObject.class != type,
            LOGGER, DynamicConfigTypeException.class,
            this.getClass(), key, type);

        // Extract config information.
        final JsonObject raw = config.getJsonObject(key);
        this.endpoint = Fn.getNull(null, () -> raw.getString(KEY_ENDPOINT), raw.getValue(KEY_ENDPOINT));
        this.config = Fn.getNull(new JsonObject(), () -> raw.getJsonObject(KEY_CONFIG), raw.getValue(KEY_CONFIG));
        // Verify the config data.
        if (null != rule) {
            Fn.outUp(() -> Fn.safeZero(() -> Ruler.verify(rule, this.config), this.config), LOGGER);
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
