package io.vertx.up.atom.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author lang
 * Stream tool for JsonArray
 */
public class Uarr {
    private static final Annal LOGGER = Annal.get(Uarr.class);

    private final transient JsonArray arrayReference;

    private Uarr(final JsonArray jsonArray) {
        this.arrayReference = Fn.getNull(new JsonArray(), () ->
            new JsonArray(jsonArray.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .collect(Collectors.toList())), jsonArray);
        LOGGER.debug(StreamInfo.STREAM_START, String.valueOf(this.hashCode()), jsonArray);
    }

    public static Uarr create(final JsonArray item) {
        return new Uarr(item);
    }

    public Uarr append(final JsonObject object) {
        this.arrayReference.add(object);
        return this;
    }

    public Uarr convert(final String from, final String to) {
        Self.convert(this.arrayReference, new ConcurrentHashMap<String, String>() {{
            this.put(from, to);
        }}, false);
        return this;
    }

    public <I, O> Uarr convert(final String field, final Function<I, O> function) {
        Self.convert(this.arrayReference, field, function, false);
        return this;
    }

    public Uarr filter(final Predicate<JsonObject> testFun) {
        Self.filter(this.arrayReference, testFun, false);
        return this;
    }

    public Uarr filter(final String field, final Object expected) {
        Self.filter(this.arrayReference, (item) -> {
            final Object actual = item.getValue(field);
            return (null != expected && expected.equals(actual));
        }, false);
        return this;
    }

    public Uarr dft(final String field, final Object value) {
        Self.defaultValue(this.arrayReference, field, value, false);
        return this;
    }

    public Uarr dft(final JsonObject values) {
        Self.defaultValue(this.arrayReference, values, false);
        return this;
    }

    public Uarr distinct() {
        Self.distinct(this.arrayReference, false);
        return this;
    }

    public Uarr sort() {
        Self.distinct(this.arrayReference, false);
        return this;
    }

    public Uarr remove(final String... keys) {
        Self.remove(this.arrayReference, false, keys);
        return this;
    }

    public Uarr vertical(final String field) {
        Self.vertical(this.arrayReference, field, false);
        return this;
    }

    public Uarr copy(final String from, final String to) {
        Self.copy(this.arrayReference, from, to, false);
        return this;
    }

    public Uarr zip(final JsonArray array, final String fromKey, final String toKey) {
        Dual.zip(this.arrayReference, array, fromKey, toKey);
        return this;
    }

    public Uarr zip(final JsonArray target) {
        Dual.zip(this.arrayReference, target);
        return this;
    }

    public JsonArray to() {
        LOGGER.debug(StreamInfo.STREAM_END, String.valueOf(this.hashCode()), this.arrayReference);
        return this.arrayReference;
    }

    public Future<JsonArray> toFuture() {
        return Future.succeededFuture(this.to());
    }

    @Override
    public String toString() {
        return this.arrayReference.encode();
    }
}
