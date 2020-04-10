package io.vertx.up.commune.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Type Mapping here
 * Definition for type conversation of `DualItem`
 */
class DualType {

    private static final ConcurrentMap<String, Class<?>> TYPES = new ConcurrentHashMap<String, Class<?>>() {
        {
            this.put("BOOLEAN", Boolean.class);
            this.put("INT", Integer.class);
            this.put("LONG", Long.class);
            this.put("DECIMAL", BigDecimal.class);
            this.put("DATE1", LocalDate.class);
            this.put("DATE2", LocalDateTime.class);
            this.put("DATE3", Long.class);
            this.put("DATE4", LocalTime.class);
        }
    };

    static Class<?> type(final String typeFlag) {
        return TYPES.get(typeFlag);
    }
}
