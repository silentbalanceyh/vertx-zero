package io.vertx.tp.plugin.excel.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.cell.ExValue;
import io.vertx.up.commune.Json;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ExRecord implements Serializable, Json {

    private final transient Map<String, Object> data
        = new HashMap<>();

    public static boolean isEmpty(final JsonObject recordRef) {
        final boolean isEmpty = recordRef.isEmpty();
        if (isEmpty) {
            return true;
        } else {
            /*
             * Remove ""
             */
            final long counter = recordRef.fieldNames().stream()
                .filter(field -> Objects.nonNull(recordRef.getValue(field)))
                .filter(field -> Ut.notNil(recordRef.getValue(field).toString()))
                .count();
            return 0 == counter;
        }
    }

    public void put(final String field, final Object value) {
        this.data.put(field, ExValue.get(value).to(value));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String field) {
        final Object value = this.data.get(field);
        return null == value ? null : (T) value;
    }

    public boolean isEmpty() {
        return isEmpty(this.toJson());
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        this.data.forEach((key, value) -> content.append(key).append(Strings.EQUAL).append(value).append(Strings.COMMA));
        return content.toString();
    }

    @Override
    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        this.data.forEach((key, value) -> this.consume(key, value, json::put));
        return json;
    }

    @Override
    public void fromJson(final JsonObject json) {
        if (null != json) {
            this.data.clear();
            json.stream().forEach(entry -> this.consume(entry.getKey(), entry.getValue(), this.data::put));
        }
    }

    private void consume(final String key, final Object value, final BiConsumer<String, Object> consumer) {
        if (Objects.nonNull(value)) {
            consumer.accept(key, value);
        }
    }
}
