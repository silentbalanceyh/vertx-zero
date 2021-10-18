package io.vertx.up.uca.adminicle;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.BiMapping;
import io.vertx.up.util.Ut;

public class FieldMapper implements Mapper {

    private final transient boolean keepNil;

    public FieldMapper() {
        this(true);
    }

    public FieldMapper(final boolean keepNil) {
        this.keepNil = keepNil;
    }

    @Override
    public JsonObject in(final JsonObject in, final BiMapping mapping) {
        return Ut.aiIn(in, mapping, this.keepNil);
    }

    @Override
    public JsonObject out(final JsonObject out, final BiMapping mapping) {
        return Ut.aiOut(out, mapping, this.keepNil);
    }
}
