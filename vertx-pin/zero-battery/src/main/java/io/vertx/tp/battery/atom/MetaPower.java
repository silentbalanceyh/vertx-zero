package io.vertx.tp.battery.atom;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Modulat;
import io.vertx.up.commune.Json;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MetaPower implements Serializable, Json {

    private static final ConcurrentMap<String, MetaPower> POWER_POOL = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Class<?>> TYPE_MAP = new ConcurrentHashMap<>() {
        {
            // Common string configuration here
            this.put("STRING", String.class);

            // Integer Here for configuration
            this.put("INTEGER", Integer.class);

            // Boolean here for enable/disable configuration
            this.put("BOOLEAN", Boolean.class);

            // Float here for decimal processing
            this.put("DECIMAL", BigDecimal.class);

            // TIME / DATE / DATETIME
            this.put("TIME", LocalTime.class);
            this.put("DATE", LocalDate.class);
            this.put("DATETIME", LocalDateTime.class);
        }
    };

    private final transient ConcurrentMap<String, Object> storedData = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, Class<?>> storedType = new ConcurrentHashMap<>();
    private final transient JsonObject storedJson = new JsonObject();

    private final transient String appId;

    /*
     * MetaPower for modulat
     */
    private MetaPower(final String appId, final JsonObject storedJson) {
        final JsonObject normalized = Ut.valueJObject(storedJson);
        this.appId = appId;
        this.storedJson.mergeIn(normalized, true);
        /*
         * Field `field = value`
         * __metadata captured `field = Class<?>`
         */
        final JsonObject metadata = Ut.valueJObject(normalized, KName.__.METADATA);
        normalized.fieldNames().forEach(field -> {
            /*
             * value processing
             */
            final Object value = normalized.getValue(field);
            this.storedData.put(field, value);
            /*
             * storedType processing
             */
            final String typeStr = metadata.getString(field);
            final Class<?> clazz = TYPE_MAP.getOrDefault(typeStr, String.class);
            this.storedType.put(field, clazz);
        });
    }

    /*
     * static initialize by appId when delay extracing
     */
    public static Future<MetaPower> initialize(final String appId) {
        /*
         * Ke.channel
         */
        if (POWER_POOL.containsKey(appId)) {
            return Ux.future(POWER_POOL.getOrDefault(appId, null));
        } else {
            return Ke.channel(Modulat.class, JsonObject::new, modulat -> modulat.extension(appId)).compose(config -> {
                if (config.containsKey(KName.__.METADATA)) {
                    final MetaPower power = new MetaPower(appId, config);
                    POWER_POOL.put(appId, power);
                    return Ux.future(power);
                } else {
                    return Ux.future();
                }
            });
        }
    }

    @SuppressWarnings("all")
    public <T> T value(final String field) {
        final Object value = this.storedData.getOrDefault(field, null);
        if (Objects.nonNull(value)) {
            final Class<?> type = this.storedType.getOrDefault(field, String.class);
            return (T) Ut.aiValue(value.toString(), type);
        } else {
            return null;
        }
    }

    public String appId() {
        return this.appId;
    }

    @Override
    public JsonObject toJson() {
        return this.storedJson.copy();
    }

    @Override
    public void fromJson(final JsonObject json) {
        throw new _501NotSupportException(this.getClass());
    }
}
