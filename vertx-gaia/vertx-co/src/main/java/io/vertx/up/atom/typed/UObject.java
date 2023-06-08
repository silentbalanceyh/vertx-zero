package io.vertx.up.atom.typed;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author lang
 * Stream tool for JsonObject
 */
public class UObject {

    private static final Annal LOGGER = Annal.get(UObject.class);

    private final JsonObject objectReference;

    private UObject(final JsonObject json) {
        this.objectReference = Fn.runOr(new JsonObject(), () -> json, json);
        LOGGER.debug(StreamInfo.STREAM_START, String.valueOf(this.hashCode()), json);
    }

    public static UObject create(final String field, final Object value) {
        return new UObject(new JsonObject().put(field, value));
    }

    public static UObject create() {
        return new UObject(new JsonObject());
    }

    public static UObject create(final JsonObject item) {
        return new UObject(item);
    }

    public UObject append(final JsonObject object) {
        UArrayInternal.append(this.objectReference, object, false);
        return this;
    }

    public UObject append(final JsonArray array) {
        UArrayInternal.append(this.objectReference, array);
        return this;
    }

    public UObject append(final String field, final Object value) {
        this.objectReference.put(field, value);
        return this;
    }

    public UObject convert(final String from, final String to) {
        UObjectInternal.convert(this.objectReference, new ConcurrentHashMap<String, String>() {{
            this.put(from, to);
        }}, false);
        return this;
    }

    public UObject dft(final String field, final Object value) {
        UObjectInternal.defaultValue(this.objectReference, field, value, false);
        return this;
    }

    public UObject dft(final JsonObject values) {
        UObjectInternal.defaultValue(this.objectReference, values, false);
        return this;
    }

    public UObject plus(final String from, final Integer seed) {
        final Object value = this.objectReference.getValue(from);
        if (null != value && Ut.isInteger(value)) {
            final Integer old = this.objectReference.getInteger(from);
            this.objectReference.put(from, old + seed);
        }
        return this;
    }

    public <I, O> UObject convert(final String field, final Function<I, O> function) {
        UObjectInternal.convert(this.objectReference, field, function, false);
        return this;
    }

    public UObject copy(final String from, final String to) {
        UObjectInternal.copy(this.objectReference, from, to, false);
        return this;
    }

    public UObject remove(final String... keys) {
        UObjectInternal.remove(this.objectReference, false, keys);
        return this;
    }

    public UObject pickup(final String... keys) {
        UObjectInternal.pickup(this.objectReference, keys);
        return this;
    }

    public UObject denull() {
        UObjectInternal.deNull(this.objectReference, false);
        return this;
    }

    public JsonObject to() {
        LOGGER.debug(StreamInfo.STREAM_END, String.valueOf(this.hashCode()), this.objectReference);
        return this.objectReference;
    }

    public Future<JsonObject> toFuture() {
        return Future.succeededFuture(this.to());
    }

    public Object get(final String field) {
        return this.objectReference.getValue(field);
    }

    @Override
    public String toString() {
        return this.objectReference.encode();
    }
}
