package io.modello.dynamic.modular.change;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractAdjuster implements Adjuster {
    private static final JsonObject ADJUST = Ao.adjuster();

    protected transient Class<?> type;

    public AbstractAdjuster(final Class<?> type) {
        this.type = type;
    }

    private JsonObject config() {
        final String simpleName = this.type.getSimpleName();
        final JsonObject config = ADJUST.getJsonObject(simpleName);
        return Ut.isNil(config) ? new JsonObject()
            .put(KName.OUT, new JsonObject())
            .put(KName.IN, new JsonObject())
            : config.copy();
    }

    protected JsonObject configOut() {
        return this.config().getJsonObject(KName.OUT);
    }

    protected JsonObject configIn() {
        return this.config().getJsonObject(KName.IN);
    }

    protected String literal(final Object input) {
        return Objects.isNull(input) ? null : input.toString().trim();
    }

    @Override
    public Object inValue(final Object ucmdbInput) {
        return Ut.aiJValue(ucmdbInput);
    }

    @Override
    public Object outValue(final Object input) {
        return Ut.aiValue(input, Date.class);
    }

    @Override
    public boolean isSame(final Object oldValue, final Object newValue) {
        return this.isSame(oldValue, newValue,
            /*
             * 基本比较
             */
            () -> oldValue.toString().trim().equals(newValue.toString().trim()));
    }

    protected boolean isSame(final Object oldValue, final Object newValue, final Supplier<Boolean> fnCompare) {
        if (Objects.isNull(oldValue) && Objects.isNull(newValue)) {
            return true;
        } else if (Objects.nonNull(oldValue) && Objects.nonNull(newValue)) {
            /*
             * 基本比较
             */
            return fnCompare.get();
        } else {
            final String oldStr = Objects.isNull(oldValue) ? VString.EMPTY : oldValue.toString().trim();
            final String newStr = Objects.isNull(newValue) ? VString.EMPTY : newValue.toString().trim();
            return oldStr.equals(newStr);
        }
    }

}
