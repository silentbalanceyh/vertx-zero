package io.vertx.tp.plugin.feign;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.codec.JsonObjectDecoder;
import feign.codec.JsonObjectEncoder;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.init.TpConfig;

import java.io.Serializable;

/**
 * Plugin for feign client
 */
public class FeignDepot implements Serializable {

    private static final Cc<String, FeignDepot> CC_CACHE = Cc.open();
    // Default configuration of feign
    private static final JsonObject DEFAULTS = new JsonObject();
    private static final JsonObject OPTIONS = new JsonObject();

    static {
        // DEFAULTS
        if (DEFAULTS.isEmpty()) {
            DEFAULTS.put("period", 5000);
            DEFAULTS.put("maxPeriod", 5000);
            DEFAULTS.put("attempts", 3);
        }
        // OPTIONS
        if (OPTIONS.isEmpty()) {
            OPTIONS.put("connect", 1000);
            OPTIONS.put("read", 3500);
        }
    }

    private final transient TpConfig reference;
    /**
     * request options
     */
    private transient Request.Options options;
    /**
     * retry default configuration
     */
    private transient Retryer.Default defaults;

    private FeignDepot(final String key, final String rule) {
        this.reference = TpConfig.create(key, rule);
        // Initializing
        this.initOpts(this.reference.getConfig());
    }

    public static FeignDepot create(final String key, final String rule) {
        return CC_CACHE.pick(() -> new FeignDepot(key, rule), key);
        // Fn.po?l(CACHE, key, () -> new FeignDepot(key, rule));
    }

    /**
     * @param clazz Input FeignApi class.
     * @param <T>   FeignApi type
     *
     * @return Feign Api reference.
     */
    public <T> T build(final Class<T> clazz) {
        return this.build(clazz, this.reference.getEndPoint(), null);
    }

    public <T> T build(final Class<T> clazz, final String endpoint) {
        return this.build(clazz, endpoint, null);
    }

    public <T> T build(final Class<T> clazz, final String endpoint, final ErrorDecoder decoder) {
        final Feign.Builder builder = Feign.builder();
        if (null != this.options) {
            builder.options(this.options);
        }
        if (null != this.defaults) {
            builder.retryer(this.defaults);
        }
        builder.encoder(new JsonObjectEncoder());
        builder.decoder(new JsonObjectDecoder());
        if (null != decoder) {
            builder.errorDecoder(decoder);
        }
        return builder.target(clazz, endpoint);
    }

    public String getEndpoint() {
        return this.reference.getEndPoint();
    }

    public JsonObject getConfig() {
        return this.reference.getConfig();
    }

    private void initOpts(final JsonObject raw) {
        // Options
        JsonObject normalized = OPTIONS;
        if (raw.containsKey("timeout")) {
            final JsonObject options = raw.getJsonObject("timeout");
            normalized = normalized.mergeIn(options);
        }
        this.options = new Request.Options(
            normalized.getInteger("connect"),
            normalized.getInteger("read"));
        // Defaults
        normalized = DEFAULTS;
        if (raw.containsKey("retry")) {
            final JsonObject defaults = raw.getJsonObject("retry");
            normalized = normalized.mergeIn(defaults);
        }
        this.defaults = new Retryer.Default(
            normalized.getInteger("period"),
            normalized.getInteger("maxPeriod"),
            normalized.getInteger("attempts")
        );
    }
}
