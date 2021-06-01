package io.vertx.up.atom.record;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class AptBatch implements AptOp<JsonArray> {
    private transient final JsonArray original;
    private transient final JsonArray current;
    private transient final JsonArray data = new JsonArray();
    private transient final ConcurrentMap<ChangeFlag, JsonArray> combine;
    private transient ChangeFlag flag;

    AptBatch(final JsonArray original, final JsonArray current, final String field) {
        this.original = Ut.sureJArray(original);
        this.current = Ut.sureJArray(current);
        this.combine = new ConcurrentHashMap<>();
        if (Ut.isNil(original)) {
            /*
             * ADD
             */
            this.flag = ChangeFlag.ADD;
            this.data.addAll(this.current.copy());
        } else if (Ut.isNil(current)) {
            /*
             * DELETE
             */
            this.flag = ChangeFlag.DELETE;
            this.data.addAll(this.original.copy());
        } else {
            /*
             * UPDATE
             * the `data` won't be initialized
             */
            this.flag = ChangeFlag.UPDATE;
            /*
             * Mode compared by field
             */
            if (Ut.isNil(field)) {
                this.data.addAll(this.original.copy());
            } else {
                /*
                 * original / current
                 * must be updated for element here, it means that
                 * 1) data is ( original + current )
                 * 2) Append mode, the original is base elements
                 */
                final JsonArray data = new JsonArray();
                Ut.itJArray(original).forEach(old -> {
                    /*
                     * 1) Get fieldValue from each old element
                     * 2) Try to find in current data.
                     */
                    final Object fieldValue = old.getValue(field);
                    if (Objects.nonNull(fieldValue)) {
                        final JsonObject found = Ut.itJArray(current).filter(item ->
                                fieldValue.equals(item.getValue(field))).findAny().orElse(null);
                        if (Ut.isNil(found)) {
                            /*
                             * Not found
                             */
                            data.add(old.copy());
                        } else {
                            /*
                             * Found merged ( old, found )
                             */
                            final JsonObject element = new JsonObject();
                            element.mergeIn(old.copy(), true).mergeIn(found, true);
                            data.add(element);
                        }
                    }
                });
                this.data.addAll(data);
            }
        }
    }

    @Override
    public JsonArray original() {
        return this.original;
    }

    @Override
    public JsonArray current() {
        return this.current;
    }

    @Override
    public JsonArray current(final JsonArray current) {
        this.current.clear();
        this.current.addAll(current);
        return this.current;
    }

    @Override
    public JsonArray data() {
        return this.data;
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
        final JsonObject inputData = Ut.sureJObject(input);
        if (Ut.notNil(inputData)) {
            /*
             * Update current JsonArray by
             *
             */
            final JsonArray normalized = new JsonArray();
            Ut.itJArray(this.data).forEach(json -> {
                final JsonObject reference = json.copy();
                reference.mergeIn(inputData.copy(), true);
                normalized.add(reference);
            });

            this.data.clear();
            this.data.addAll(normalized.copy());
            /*
             * DELETE -> UPDATE
             */
            if (this.current.isEmpty()) {
                this.flag = ChangeFlag.UPDATE;
                this.current.addAll(normalized.copy());
            }
        }
        return this;
    }

    @Override
    public ConcurrentMap<ChangeFlag, JsonArray> compared() {
        return this.combine;
    }
}
