package io.vertx.up.uca.adminicle;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.DualItem;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class FieldMapper implements Mapper {

    private final transient boolean keepNil;

    public FieldMapper() {
        this(true);
    }

    public FieldMapper(final boolean keepNil) {
        this.keepNil = keepNil;
    }

    @Override
    public JsonObject in(final JsonObject in, final DualItem mapping) {
        if (Objects.isNull(mapping)) {
            /*
             * No mapping
             */
            return in.copy();
        } else {
            /*
             * Mapping configured
             */
            final JsonObject normalized = new JsonObject();
            in.fieldNames().forEach(field -> {
                /*
                 * field is (To) field,
                 * convert to standard model attribute
                 */
                final String fromField = mapping.from(field);
                if (Ut.isNil(fromField)) {
                    if (this.keepNil) {
                        normalized.put(field, in.getValue(field));
                    }
                } else {
                    normalized.put(fromField, in.getValue(field));
                }
            });
            return normalized;
        }
    }

    @Override
    public JsonObject out(final JsonObject out, final DualItem mapping) {
        if (Objects.isNull(mapping)) {
            /*
             * No mapping
             */
            return out.copy();
        } else {
            final JsonObject normalized = new JsonObject();
            out.fieldNames().forEach(field -> {
                /*
                 * field is (From) field
                 */
                final String fromField = mapping.to(field);
                if (Ut.isNil(fromField)) {
                    if (this.keepNil) {
                        normalized.put(field, out.getValue(field));
                    }
                } else {
                    normalized.put(fromField, out.getValue(field));
                }
            });
            return normalized;
        }
    }
}
