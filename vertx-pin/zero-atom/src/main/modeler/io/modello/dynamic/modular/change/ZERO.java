package io.modello.dynamic.modular.change;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Class<?>, Adjuster> POOL_ADJUST = new ConcurrentHashMap<Class<?>, Adjuster>() {
        {
            this.put(String.class, new AjString());
            this.put(Integer.class, new AjInteger());
            this.put(Long.class, new AjLong());
            this.put(Boolean.class, new AjBoolean());
            this.put(BigDecimal.class, new AjBigDecimal());
            this.put(LocalTime.class, new AjLocalTime());
            this.put(LocalDate.class, new AjLocalDate());
            this.put(LocalDateTime.class, new AjLocalDateTime());
        }
    };
}
