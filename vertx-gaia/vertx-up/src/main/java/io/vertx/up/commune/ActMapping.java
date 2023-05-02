package io.vertx.up.commune;

import io.horizon.eon.em.uca.AopType;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.BTree;
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
        if (AopType.BEFORE != mapping.getMode() && AopType.AROUND != mapping.getMode()) {
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
        if (AopType.AFTER != mapping.getMode() && AopType.AROUND != mapping.getMode()) {
            return false;
        }
        return mapping.valid();
    }

    protected Mapper mapper() {
        return this.mapper;
    }
}
