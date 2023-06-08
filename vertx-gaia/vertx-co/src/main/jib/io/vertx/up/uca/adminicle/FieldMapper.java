package io.vertx.up.uca.adminicle;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;
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
    public JsonObject in(final JsonObject in, final BMapping mapping) {
        return Ut.aiIn(in, mapping, this.keepNil);
    }

    @Override
    public JsonObject out(final JsonObject out, final BMapping mapping) {
        return Ut.aiOut(out, mapping, this.keepNil);
    }
}
