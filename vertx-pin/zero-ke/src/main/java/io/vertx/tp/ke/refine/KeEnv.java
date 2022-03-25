package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.environment.Indent;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeEnv {

    private static final Set<String> FIELDS = new HashSet<>() {
        {
            this.add(KName.SIGMA);
            this.add(KName.LANGUAGE);
            this.add(KName.ACTIVE);
            this.add(KName.CREATED_AT);
            this.add(KName.CREATED_BY);
            this.add(KName.UPDATED_AT);
            this.add(KName.UPDATED_BY);
        }
    };

    static <T, I> void audit(final I output, final String outPojo, final T input, final String inPojo, final boolean isUpdated) {
        // If contains pojo, must be deserialized for auditor information
        // OutMap Calculation
        final ConcurrentMap<String, String> outMap = buildMap(outPojo, isUpdated);
        final ConcurrentMap<String, String> inMap = buildMap(inPojo, isUpdated);
        /* Mapping */
        final LocalDateTime now = LocalDateTime.now();
        outMap.forEach((key, out) -> {
            final String in = inMap.get(key);
            if (KName.CREATED_AT.equals(in) || KName.UPDATED_AT.equals(in)) {
                // LocalDataTime
                Ut.field(output, out, now);
            } else {
                // Copy Data
                Ut.field(output, out, Ut.field(input, in));
            }
        });
    }

    private static ConcurrentMap<String, String> buildMap(final String filename, final boolean isUpdated) {
        final ConcurrentMap<String, String> vector = new ConcurrentHashMap<>();
        if (Ut.isNil(filename)) {
            FIELDS.forEach(each -> vector.put(each, each));
        } else {
            final Mojo outMojo = Mirror.create(KeEnv.class).mount(filename).mojo();
            outMojo.getIn().forEach((in, out) -> {
                if (FIELDS.contains(in)) {
                    vector.put(in, out);
                }
            });
        }
        if (isUpdated) {
            vector.remove(KName.CREATED_BY);
            vector.remove(KName.CREATED_AT);
        }
        return vector;
    }

    static <T> Future<List<T>> indent(final List<T> input, final String sigma,
                                      final String code,
                                      final BiConsumer<T, String> fnConsumer) {
        if (Objects.isNull(input) || input.isEmpty()) {
            return Ux.future(new ArrayList<>());
        } else {
            return KeChannel.channel(Indent.class, () -> input, stub ->
                stub.indent(code, sigma, input.size()).compose(queue -> {
                    input.forEach(entity -> fnConsumer.accept(entity, queue.poll()));
                    return Ux.future(input);
                }));
        }
    }

    static <T> Future<T> indent(final T input, final String sigma,
                                final String code,
                                final BiConsumer<T, String> fnConsumer) {
        return KeChannel.channel(Indent.class, () -> input, stub -> {
            if (Ut.isNil(sigma)) {
                return Ux.future(input);
            } else {
                return stub.indent(code, sigma)
                    .compose(indent -> {
                        fnConsumer.accept(input, indent);
                        return Ux.future(input);
                    });
            }
        });
    }

    /*
     * Indent Single
     */
    static Future<JsonObject> indent(final JsonObject data, final String code) {
        return KeChannel.channel(Indent.class, () -> data, stub -> {
            final String sigma = data.getString(KName.SIGMA);
            if (Ut.isNil(sigma) || Ut.isNil(code)) {
                return Ux.future(data);
            } else {
                return stub.indent(code, sigma)
                    .compose(indent -> Ux.future(data.put(KName.INDENT, indent)));
            }
        });
    }

    static Future<JsonArray> indent(final JsonArray data, final String code) {
        return KeChannel.channel(Indent.class, () -> data, stub -> {
            final String sigma = Ut.valueString(data, KName.SIGMA);
            if (Ut.isNil(sigma)) {
                return Ux.future(data);
            } else {
                return stub.indent(code, sigma, data.size())
                    .compose(indentQ -> {
                        Ut.itJArray(data).forEach(json -> json.put(KName.INDENT, indentQ.poll()));
                        return Ux.future(data);
                    });
            }
        });
    }
}
