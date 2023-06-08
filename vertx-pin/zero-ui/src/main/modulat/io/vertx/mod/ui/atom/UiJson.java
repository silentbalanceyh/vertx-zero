package io.vertx.mod.ui.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Arrays;

public class UiJson {

    private transient JsonObject input;

    private UiJson(final JsonObject input) {
        if (Ut.isNil(input)) {
            this.input = new JsonObject();
        } else {
            /* Copy to avoid the same reference issue */
            this.input = input.copy();
        }
    }

    public static UiJson create(final JsonObject input) {
        return new UiJson(input);
    }

    public UiJson add(final String key, final Object value) {
        this.input.put(key, value);
        return this;
    }

    public UiJson remove(final String... keys) {
        Arrays.stream(keys).forEach(this.input::remove);
        return this;
    }

    public UiJson removeWith(final String prefix) {
        final JsonObject processed = this.input.copy();
        processed.fieldNames().stream()
            .filter(key -> key.startsWith(prefix))
            .forEach(this.input::remove);
        return this;
    }

    public UiJson pickup(final String... keys) {
        final JsonObject processed = this.input.copy();
        this.input.clear();
        Arrays.stream(keys).forEach(key -> this.input.put(key, processed.getValue(key)));
        return this;
    }

    public UiJson pickupWith(final String prefix) {
        final JsonObject processed = this.input.copy();
        this.input.clear();
        processed.fieldNames().stream()
            .filter(key -> key.startsWith(prefix))
            .forEach(key -> this.input.put(key, processed.getValue(key)));
        return this;
    }

    public UiJson convertChild(final String from, final String prefix) {
        final JsonObject processed = this.input.copy();
        final JsonObject item = processed.getJsonObject(from);
        if (Ut.isNotNil(item)) {
            item.fieldNames().stream().filter(field -> field.startsWith(prefix))
                .forEach(key -> this.input.put(key, item.getValue(key)));
        }
        this.input.remove(from);
        return this;
    }

    public UiJson replaceWith(final String from, final String to) {
        final JsonObject processed = this.input.copy();
        this.input.clear();
        processed.fieldNames().stream()
            .filter(key -> key.startsWith(from))
            .forEach(key -> {
                final String replaced = key.replace(from, to);
                this.input.put(replaced, processed.getValue(key));
            });
        return this;
    }

    public UiJson convert(final String from, final String to) {
        if (this.input.containsKey(from)) {
            final Object value = this.input.getValue(from);
            this.input.remove(from);
            this.input.put(to, value);
        }
        return this;
    }

    public JsonObject to() {
        /* Copy the result here */
        final JsonObject to = this.input.copy();
        this.input = null;      // Gc clean up
        return to;
    }
}
