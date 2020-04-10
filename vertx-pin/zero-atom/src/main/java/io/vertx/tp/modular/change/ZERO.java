package io.vertx.tp.modular.change;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Class<?>, Adjuster> POOL_ADJUST = new ConcurrentHashMap<Class<?>, Adjuster>() {
        {
            this.put(String.class, new StringAj(String.class));
            this.put(Integer.class, new IntegerAj(Integer.class));
            this.put(Long.class, new LongAj(Long.class));
            this.put(Boolean.class, new BooleanAj(Boolean.class));
            this.put(BigDecimal.class, new BigDecimalAj(BigDecimal.class));
            this.put(LocalTime.class, new LocalTimeAj(LocalTime.class));
            this.put(LocalDate.class, new LocalDateAj(LocalDate.class));
            this.put(LocalDateTime.class, new LocalDateTimeAj(LocalDateTime.class));
        }
    };
}
