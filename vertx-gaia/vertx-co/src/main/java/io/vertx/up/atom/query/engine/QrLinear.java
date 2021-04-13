package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._400OpUnsupportException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.ArrayList;
import java.util.List;

public class QrLinear implements QrDo {

    private static final Annal LOGGER = Annal.get(QrLinear.class);
    private final List<Kv<String, Kv<String, Object>>> conditions = new ArrayList<>();
    private final transient JsonObject raw = new JsonObject();

    private QrLinear(final JsonObject data) {
        this.raw.mergeIn(data.copy());
        for (final String field : data.fieldNames()) {
            // Add
            this.add(field, data.getValue(field));
        }
    }

    public static QrLinear create(final JsonObject data) {
        return new QrLinear(data);
    }

    public List<Kv<String, Kv<String, Object>>> getConditions() {
        return this.conditions;
    }


    /**
     * Check current QTree to see whether it's valid.
     *
     * @return {@link java.lang.Boolean};
     */
    @Override
    public boolean valid() {
        return !this.conditions.isEmpty();
    }

    /**
     * Add new condition to LINEAR component.
     *
     * The parameters are following format:
     *
     * 1. fieldExpr is format of `field,op`, it contains two parts.
     * 2. fieldExpr is format of `field`, the default op is =.
     *
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link QrLinear}
     */
    @Override
    public QrLinear add(final String fieldExpr, final Object value) {
        // Field add
        final String filterField;
        final String op;
        if (fieldExpr.contains(Strings.COMMA)) {
            filterField = fieldExpr.split(Strings.COMMA)[0];
            op = fieldExpr.split(Strings.COMMA)[1];
        } else {
            filterField = fieldExpr;
            op = Qr.Op.EQ;
        }
        Fn.outWeb(!Qr.Op.VALUES.contains(op), LOGGER, _400OpUnsupportException.class, this.getClass(), op);
        final Kv<String, Object> condition = Kv.create(op, value);
        final Kv<String, Kv<String, Object>> item = Kv.create(filterField, condition);
        // At the same time.
        this.conditions.add(item);
        this.raw.put(fieldExpr, value);
        return this;
    }

    /**
     * Serialized current instance to Json
     *
     * @return {@link JsonObject}
     */
    @Override
    public JsonObject toJson() {
        return this.raw;
    }
}
