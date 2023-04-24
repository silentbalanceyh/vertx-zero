package io.aeon.experiment.channel;

import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/*
 * Stack of fluent api here to store parameters
 */
public class Income implements Serializable {
    private static final Annal LOGGER = Annal.get(Income.class);
    private final transient Vector<Object> queue = new Vector<>();
    private final transient List<String> names = new ArrayList<>();

    private <T> Income(final Class<T> key) {
        final Lexeme<T> lexeme = Pocket.get(key);
        if (Objects.nonNull(lexeme)) {
            this.names.addAll(lexeme.params());
        }
    }

    public static <T> Income in(final Class<T> key) {
        return new Income(key);
    }

    public <T> Income in(final T value) {
        // System.err.println(value);
        this.queue.add(value);
        return this;
    }

    public JsonObject arguments() {
        final JsonObject arguments = new JsonObject();
        /*
         * Length here
         */
        final int size = this.names.size();
        for (int idx = 0; idx < size; idx++) {
            /*
             * field name
             */
            final String field = this.names.get(idx);
            if (!this.queue.isEmpty()) {
                final Object value = this.queue.get(idx);
                LOGGER.debug("[ Income ] field = {0}, value = {1}", field, value);
                arguments.put(field, value);
            }
        }
        return arguments;
    }
}
