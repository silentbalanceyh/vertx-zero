package io.vertx.mod.battery.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
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
public class PowerBlock implements Serializable {

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
    private final transient String name;

    PowerBlock(final String name, final JsonObject normalized) {
        this.name = name;
        /*
         * Field `field = value`
         * __metadata captured `field = Class<?>`
         */
        final JsonObject metadata = Ut.valueJObject(normalized, KName.__.METADATA);
        normalized.fieldNames().forEach(field -> {
            /*
             * storedType processing
             */
            final String typeStr = metadata.getString(field);
            if (Ut.isNotNil(typeStr)) {
                /*
                 * value processing
                 */
                final Object value = normalized.getValue(field);
                if (Objects.nonNull(value)) {
                    // Fix: java.lang.NullPointerException
                    this.storedData.put(field, value);
                }
                final Class<?> clazz = TYPE_MAP.getOrDefault(typeStr, String.class);
                this.storedType.put(field, clazz);
            }
        });
    }

    @SuppressWarnings("all")
    public <T> T value(final String field) {
        return value(field, null);
    }

    @SuppressWarnings("all")
    public <T> T value(final String field, final T defaultValue) {
        final Object value = this.storedData.getOrDefault(field, null);
        if (Objects.nonNull(value)) {
            final Class<?> type = this.storedType.getOrDefault(field, String.class);
            return (T) Ut.aiValue(value.toString(), type);
        } else {
            return defaultValue;
        }
    }

    public String name() {
        return this.name;
    }
}
