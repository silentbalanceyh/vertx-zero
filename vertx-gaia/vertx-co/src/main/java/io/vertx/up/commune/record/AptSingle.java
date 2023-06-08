package io.vertx.up.commune.record;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Package scope usage for json data here.
 */
class AptSingle implements AptOp<JsonObject> {
    private final ChangeFlag flag;
    private final JsonObject original;
    private final JsonObject current = new JsonObject();
    private final ConcurrentMap<ChangeFlag, JsonObject> combine = new ConcurrentHashMap<>();

    // Refer the same as the UPDATE in combine
    private final JsonObject replaced = new JsonObject();
    private final JsonObject appended = new JsonObject();

    AptSingle(final JsonObject original, final JsonObject current) {
        this.original = original;
        if (Objects.isNull(original)) {
            this.current.mergeIn(current.copy(), true);
            /* ADD */
            this.flag = ChangeFlag.ADD;
            this.combine.put(ChangeFlag.ADD, current.copy());
        } else if (Objects.isNull(current)) {
            /* DELETE */
            this.flag = ChangeFlag.DELETE;
            this.combine.put(ChangeFlag.DELETE, original.copy());
        } else {
            this.current.mergeIn(current.copy(), true);
            // Calculate
            this.setInternal(this.current);
            this.flag = ChangeFlag.UPDATE;
        }
    }


    @Override
    public JsonObject dataO() {
        return this.original;
    }

    @Override
    public JsonObject dataN() {
        return this.current;
    }

    @Override
    public JsonObject dataS() {
        return this.replaced;
    }

    @Override
    public JsonObject dataA() {
        return this.appended;
    }

    @Override
    public AptOp<JsonObject> update(final JsonObject input) {
        final JsonObject normalized = Ut.valueJObject(input);
        final JsonObject dataRef = this.combine.get(this.flag);
        dataRef.mergeIn(normalized, true);
        /* Double Confirmation */
        this.combine.put(this.flag, dataRef);
        return this;
    }

    @Override
    public JsonObject set(final JsonObject current) {
        this.current.clear();
        this.current.mergeIn(current, true);

        /* Combine for UPDATE */
        if (ChangeFlag.UPDATE == this.flag && this.combine.containsKey(ChangeFlag.UPDATE)) {
            // Calculate
            this.setInternal(this.current);
        }
        return this.current;
    }

    private void setInternal(final JsonObject current) {
        /*
         * UPDATE
         * Calculate two situations
         *
         * 1. Default, Replaced
         * 2. Appended, only the field that original does not contains added.
         */
        final JsonObject original = Ut.valueJObject(this.original).copy();

        this.replaced.clear();
        this.replaced.mergeIn(original, true);
        this.replaced.mergeIn(current, true);

        this.appended.clear();
        Ut.valueAppend(this.appended, original, current);
        // Default situation, Overwrite ( Merge )
        this.combine.put(ChangeFlag.UPDATE, this.replaced);
    }

    @Override
    public JsonObject dataI() {
        return this.combine.get(this.flag);
    }

    @Override
    public ChangeFlag type() {
        return this.flag;
    }
}
