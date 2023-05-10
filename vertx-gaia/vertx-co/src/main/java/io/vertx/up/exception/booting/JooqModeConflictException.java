package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonObject;

public class JooqModeConflictException extends BootingException {

    public JooqModeConflictException(
        final Class<?> clazz,
        final Ir.Mode required,
        final JsonObject filters) {
        super(clazz, required, filters.encode());
    }

    @Override
    public int getCode() {
        return -40058;
    }
}
