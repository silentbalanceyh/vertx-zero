package io.vertx.up.plugin.excel.cell;

import java.util.UUID;

/**
 * {UUID} Processing
 */
public class UuidValue implements ExValue {

    @Override
    @SuppressWarnings("all")
    public String to(final Object value) {
        return UUID.randomUUID().toString();
    }
}
