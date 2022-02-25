package io.vertx.tp.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.environment.Indent;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KeData {

    static <T> Future<List<T>> indent(final List<T> input, final String sigma,
                                      final String code,
                                      final BiConsumer<T, String> fnConsumer) {
        if (Objects.isNull(input) || input.isEmpty()) {
            return Ux.future(new ArrayList<>());
        } else {
            return KeRun.channel(Indent.class, () -> input, stub ->
                stub.indent(code, sigma, input.size()).compose(queue -> {
                    input.forEach(entity -> fnConsumer.accept(entity, queue.poll()));
                    return Ux.future(input);
                }));
        }
    }

    static <T> Future<T> indent(final T input, final String sigma,
                                final String code,
                                final BiConsumer<T, String> fnConsumer) {
        return KeRun.channel(Indent.class, () -> input, stub -> {
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
        return KeRun.channel(Indent.class, () -> data, stub -> {
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
        return KeRun.channel(Indent.class, () -> data, stub -> {
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
