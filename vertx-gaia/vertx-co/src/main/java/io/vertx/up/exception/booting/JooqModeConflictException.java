package io.vertx.up.exception.booting;

import io.horizon.exception.BootingException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;

public class JooqModeConflictException extends BootingException {

    public JooqModeConflictException(
        final Class<?> clazz,
        final Qr.Mode required,
        final JsonObject filters) {
        super(clazz, required, filters.encode());
    }

    @Override
    public int getCode() {
        return -40058;
    }
}
