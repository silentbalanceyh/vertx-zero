package io.vertx.up.atom.typed;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author lang
 * Stream tool for JsonArray
 */
public class UArray {
    private static final Annal LOGGER = Annal.get(UArray.class);

    private final JsonArray arrayReference;

    private UArray(final JsonArray jsonArray) {
        this.arrayReference = Fn.runOr(new JsonArray(), () ->
            new JsonArray(jsonArray.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .collect(Collectors.toList())), jsonArray);
        LOGGER.debug(StreamInfo.STREAM_START, String.valueOf(this.hashCode()), jsonArray);
    }

    public static UArray create(final JsonArray item) {
        return new UArray(item);
    }

    public UArray append(final JsonObject object) {
        this.arrayReference.add(object);
        return this;
    }

    public UArray convert(final String from, final String to) {
        UObjectInternal.convert(this.arrayReference, new ConcurrentHashMap<String, String>() {{
            this.put(from, to);
        }}, false);
        return this;
    }

    public <I, O> UArray convert(final String field, final Function<I, O> function) {
        UObjectInternal.convert(this.arrayReference, field, function, false);
        return this;
    }

    public UArray filter(final Predicate<JsonObject> testFun) {
        UObjectInternal.filter(this.arrayReference, testFun, false);
        return this;
    }

    public UArray filter(final String field, final Object expected) {
        UObjectInternal.filter(this.arrayReference, (item) -> {
            final Object actual = item.getValue(field);
            return (null != expected && expected.equals(actual));
        }, false);
        return this;
    }

    public UArray dft(final String field, final Object value) {
        UObjectInternal.defaultValue(this.arrayReference, field, value, false);
        return this;
    }

    public UArray dft(final JsonObject values) {
        UObjectInternal.defaultValue(this.arrayReference, values, false);
        return this;
    }

    public UArray distinct() {
        UObjectInternal.distinct(this.arrayReference, false);
        return this;
    }

    public UArray sort() {
        UObjectInternal.distinct(this.arrayReference, false);
        return this;
    }

    public UArray remove(final String... keys) {
        UObjectInternal.remove(this.arrayReference, false, keys);
        return this;
    }

    public UArray vertical(final String field) {
        UObjectInternal.vertical(this.arrayReference, field, false);
        return this;
    }

    public UArray copy(final String from, final String to) {
        UObjectInternal.copy(this.arrayReference, from, to, false);
        return this;
    }

    public UArray zip(final JsonArray array, final String fromKey, final String toKey) {
        UArrayInternal.zip(this.arrayReference, array, fromKey, toKey);
        return this;
    }

    public UArray zip(final JsonArray target) {
        UArrayInternal.zip(this.arrayReference, target);
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
