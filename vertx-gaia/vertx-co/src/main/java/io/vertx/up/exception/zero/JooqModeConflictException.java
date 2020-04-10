package io.vertx.up.exception.zero;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.exception.UpException;

public class JooqModeConflictException extends UpException {

    public JooqModeConflictException(
            final Class<?> clazz,
            final Inquiry.Mode required,
            final JsonObject filters) {
        super(clazz, required, filters.encode());
    }

    @Override
    public int getCode() {
        return -40058;
    }
}
