package io.vertx.up.commune.record;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AptArray implements Serializable {
    private final JsonArray add = new JsonArray();
    private final JsonArray delete = new JsonArray();
    private final JsonArray append = new JsonArray();
    private final JsonArray replace = new JsonArray();
    private String field = "key";

    public String getField() {
        return this.field;
    }

    public void setField(final String field) {
        if (Ut.isNotNil(field)) {
            this.field = field;
        }
    }

    public JsonArray add() {
        return this.add;
    }

    public JsonArray delete() {
        return this.delete;
    }

    public JsonArray append() {
        return this.append;
    }

    public JsonArray replace() {
        return this.replace;
    }

    public void reset() {
        this.add.clear();
        this.delete.clear();
        this.append.clear();
        this.replace.clear();
    }

    public void calculate(final JsonArray original, final JsonArray current) {
        this.reset();
        /*
         * UPDATE
         * Calculate the complex situations
         *
         * 1. UPDATE means compared result
         * 2. Replaced
         * 3. Appended
         */
        Ut.itJArray(original).forEach(json -> {
            /*
             * 1) Get fieldValue from each old element
             * 2) Try to find in current data.
             */
            final Object fieldValue = json.getValue(this.field);
            if (Objects.nonNull(fieldValue)) {
                final JsonObject found = Ut.elementFind(current, this.field, fieldValue);
                final JsonObject element = json.copy();
                if (Ut.isNil(found)) {
                    /* OLD: Value, NEW: Value */
                    this.delete.add(element);
                } else {
                    /* Merged */
                    final JsonObject replaced = new JsonObject();
                    replaced.mergeIn(element, true);
                    replaced.mergeIn(found, true);
                    this.replace.add(replaced);
                    /* Appended */
                    final JsonObject appended = new JsonObject();
                    Ut.valueAppend(appended, element, found);
                    this.append.add(appended);
                }
            }
        });

        Ut.itJArray(current).forEach(json -> {
            final Object fieldValue = json.getValue(this.field);
            final JsonObject element = json.copy();
            if (Objects.nonNull(fieldValue)) {
                final JsonObject found = Ut.elementFind(original, this.field, fieldValue);
                if (Ut.isNil(found)) {
                    this.add.add(element);
                }
            } else {
                // If the critical field value is null, marked as add
                this.add.add(element);
            }
        });
    }

    public void update(final JsonObject inputData) {
        if (Ut.isNotNil(inputData)) {
            Ut.itJArray(this.replace).forEach(json -> json.mergeIn(inputData, true));
            Ut.itJArray(this.append).forEach(json -> json.mergeIn(inputData, true));
        }
    }
}
