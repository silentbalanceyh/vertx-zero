package io.vertx.up.commune;

import io.horizon.eon.em.EmAop;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.uca.adminicle.FieldMapper;
import io.vertx.up.uca.adminicle.Mapper;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

public abstract class ActMapping implements Serializable {

    private final transient Mapper mapper = new FieldMapper();

    /*
     * ActIn
     */
    protected HRecord getRecord(final Object input, final HRecord definition, final BTree mapping) {
        final HRecord record = definition.createNew();
        if (input instanceof String) {
            final String key = (String) input;
            record.key(key);
        } else if (input instanceof JsonObject) {
            final JsonObject dataRef = (JsonObject) input;
            if (Ut.isNotNil(dataRef)) {
                /*
                 * Set current data to `Record` with `DualMapping`
                 * Check whether mount dual mapping first here
                 *
                 * Passive Only
                 */
                if (this.isBefore(mapping)) {
                    final JsonObject normalized = this.mapper.in(dataRef, mapping.child());
                    record.set(normalized);
                } else {
                    record.set(dataRef.copy());
                }
            }
        }
        return record;
    }

    /*
     * Whether it's before automatic
     */
    protected boolean isBefore(final BTree mapping) {
        if (Objects.isNull(mapping)) {
            return false;
        }
        if (EmAop.Effect.BEFORE != mapping.getMode() && EmAop.Effect.AROUND != mapping.getMode()) {
            return false;
        }
        return mapping.valid();
    }

    /*
     * Whether it's after automatic
     */
    protected boolean isAfter(final BTree mapping) {
        if (Objects.isNull(mapping)) {
            return false;
        }
        if (EmAop.Effect.AFTER != mapping.getMode() && EmAop.Effect.AROUND != mapping.getMode()) {
            return false;
        }
        return mapping.valid();
    }

    protected Mapper mapper() {
        return this.mapper;
    }
}
