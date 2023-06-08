package io.vertx.up.commune.record;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class AptBatch implements AptOp<JsonArray> {
    private final JsonArray original;
    private final JsonArray current;

    private final ConcurrentMap<ChangeFlag, JsonArray> combine = new ConcurrentHashMap<>();

    // Refer the same as the UPDATE in combine
    private final AptArray array = new AptArray();
    private final ChangeFlag flag;

    AptBatch(final JsonArray original, final JsonArray current, final String field) {
        this.original = Ut.valueJArray(original);
        this.current = Ut.valueJArray(current);
        this.array.setField(field);
        if (Ut.isNil(original)) {
            /* ADD */
            this.flag = ChangeFlag.ADD;
        } else if (Ut.isNil(current)) {
            /* DELETE */
            this.flag = ChangeFlag.DELETE;
        } else {
            /* UPDATE the `data` won't be initialized */
            this.flag = ChangeFlag.UPDATE;
        }
        // Calculate
        this.setInternal(current);
    }

    @Override
    public JsonArray dataO() {
        return this.original;
    }

    @Override
    public JsonArray dataN() {
        return this.current;
    }

    @Override
    public JsonArray dataS() {
        return this.array.replace();
    }

    @Override
    public JsonArray dataA() {
        return this.array.append();
    }

    public JsonArray dataDelete() {
        return this.array.delete();
    }

    public JsonArray dataAdd() {
        return this.array.add();
    }

    @Override
    public JsonArray set(final JsonArray current) {
        this.current.clear();
        this.current.addAll(current);
        // Calculate
        this.setInternal(this.current);
        return this.current;
    }

    @Override
    public JsonArray dataI() {
        return this.combine.get(this.flag);
    }

    @Override
    public ChangeFlag type() {
        return this.flag;
    }

    /*
     * This operation will convert the flat to UPDATE directly
     * It means that only UPDATE flag could trigger the operation
     * update(input)
     *
     * 1) The situation is that JsonArray merged JsonObject
     *    Here the initialization may be DELETE, original = data, current = empty
     *    If you call update method it means that the original will be updated
     *    The flag could convert from DELETE to update
     *
     * 2) When here are DELETE operation, the update method will be ignored and could not
     *    be call here.
     */
    @Override
    public AptOp<JsonArray> update(final JsonObject input) {
        final JsonObject inputData = Ut.valueJObject(input);
        if (Ut.isNotNil(inputData)) {
            /* DELETE -> UPDATE */
            // if (this.current.isEmpty()) {
            // this.flag = ChangeFlag.UPDATE;
            // this.current.addAll(normalized.copy());
            // }
            this.array.update(inputData);
        }
        return this;
    }

    private void setInternal(final JsonArray current) {
        /*
         * UPDATE
         * Calculate the complex situations
         *
         * 1. UPDATE means compared result
         * 2. Replaced
         * 3. Appended
         */
        this.array.calculate(this.original, current);
        final JsonArray dataDft = new JsonArray();
        dataDft.addAll(this.array.delete());
        dataDft.addAll(this.array.replace());
        this.combine.put(ChangeFlag.ADD, this.array.add());
        this.combine.put(ChangeFlag.UPDATE, dataDft);
        this.combine.put(ChangeFlag.DELETE, this.array.delete());
    }
}
