package io.vertx.up.util;

import org.junit.Test;

import java.time.Instant;

public class PeriodT {
    @Test
    public void testD() {
        final Instant instant = Period.parseAt("W,17:00,2");
        System.out.println(Ut.toDateTime(instant));
    }
}
