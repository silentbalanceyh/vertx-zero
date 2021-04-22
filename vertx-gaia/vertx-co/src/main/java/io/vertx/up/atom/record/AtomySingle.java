package io.vertx.up.atom.record;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Package scope usage for json data here.
 */
class AtomySingle implements AtomyOp<JsonObject> {
    private final transient ChangeFlag flag;
    private final transient JsonObject original;
    private final transient JsonObject current = new JsonObject();
    private final transient ConcurrentMap<ChangeFlag, JsonObject> combine;

    AtomySingle(final JsonObject original, final JsonObject current) {
        this.original = original;
        this.combine = new ConcurrentHashMap<>();
        if (Objects.isNull(original)) {
            this.current.mergeIn(current.copy(), true);
            /*
             * ADD
             */
            this.flag = ChangeFlag.ADD;
            this.combine.put(ChangeFlag.ADD, current.copy());
        } else if (Objects.isNull(current)) {
            /*
             * DELETE
             */
            this.flag = ChangeFlag.DELETE;
            this.combine.put(ChangeFlag.DELETE, original.copy());
        } else {
            this.current.mergeIn(current.copy(), true);
            /*
             * UPDATE
             */
            final JsonObject combine = new JsonObject();
            combine.mergeIn(original.copy(), true);
            combine.mergeIn(current.copy(), true);

            this.flag = ChangeFlag.UPDATE;
            this.combine.put(ChangeFlag.UPDATE, combine.copy());
        }
    }

    @Override
    public AtomyOp<JsonObject> update(final JsonObject input) {
        final JsonObject normalized = Ut.sureJObject(input);
        final JsonObject dataRef = this.combine.get(this.flag);
        dataRef.mergeIn(normalized, true);
        /* Double Confirmation */
        this.combine.put(this.flag, dataRef);
        return this;
    }

    @Override
    public JsonObject original() {
        return this.original;
    }

    @Override
    public JsonObject current() {
        return this.current;
    }

    @Override
    public JsonObject current(final JsonObject current) {
        this.current.clear();
        this.current.mergeIn(current, true);
        /*
         * Combine for UPDATE
         */
        if (ChangeFlag.UPDATE == this.flag && this.combine.containsKey(ChangeFlag.UPDATE)) {
            this.combine.put(ChangeFlag.UPDATE, current.copy());
        }
        return this.current;
    }

    @Override
    public JsonObject data() {
        return this.combine.get(this.flag);
    }

    @Override
    public ChangeFlag type() {
        return this.flag;
    }

    @Override
    public ConcurrentMap<ChangeFlag, JsonObject> compared() {
        return this.combine;
    }
}
