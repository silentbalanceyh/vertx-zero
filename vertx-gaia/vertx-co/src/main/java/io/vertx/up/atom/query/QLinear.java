package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._400OpUnsupportException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.ArrayList;
import java.util.List;

public class QLinear {

    private static final Annal LOGGER = Annal.get(QLinear.class);
    private final List<Kv<String, Kv<String, Object>>> conditions = new ArrayList<>();
    private final transient JsonObject raw = new JsonObject();

    private QLinear(final JsonObject data) {
        this.raw.mergeIn(data.copy());
        for (final String field : data.fieldNames()) {
            // Add
            this.add(field, data.getValue(field));
        }
    }

    public static QLinear create(final JsonObject data) {
        return new QLinear(data);
    }

    public List<Kv<String, Kv<String, Object>>> getConditions() {
        return this.conditions;
    }

    public boolean isValid() {
        return !this.conditions.isEmpty();
    }

    public QLinear add(final String field, final Object value) {
        // Field add
        final String filterField;
        final String op;
        if (field.contains(Strings.COMMA)) {
            filterField = field.split(Strings.COMMA)[0];
            op = field.split(Strings.COMMA)[1];
        } else {
            filterField = field;
            op = Qr.Op.EQ;
        }
        Fn.outWeb(!Qr.Op.VALUES.contains(op), LOGGER,
                _400OpUnsupportException.class, this.getClass(), op);
        final Kv<String, Object> condition = Kv.create(op, value);
        final Kv<String, Kv<String, Object>> item = Kv.create(filterField, condition);
        // At the same time.
        this.conditions.add(item);
        this.raw.put(field, value);
        return this;
    }

    public JsonObject toJson() {
        return this.raw;
    }
}
