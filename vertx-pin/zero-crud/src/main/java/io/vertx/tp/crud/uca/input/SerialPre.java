package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.environment.Indent;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KField;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SerialPre implements Pre {
    private static final Annal LOGGER = Annal.get(SerialPre.class);

    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        /* Sigma Checking */
        final String sigma = data.getString(KName.SIGMA);
        if (Ut.isNil(sigma)) {
            /* Fix Bug of number generations here */
            return Ux.future(data);
        }

        /* Number generation */
        return this.run(data, in, (numbers) -> Ux.channelAsync(Indent.class, () -> Ux.future(data), stub -> {
            Ke.infoKe(LOGGER, "Table here {0}, Serial numbers {1}", in.module().getTable(), numbers.encode());
            /* Channel */
            final ConcurrentMap<String, Future<String>> numberMap = new ConcurrentHashMap<>();
            numbers.fieldNames().stream()
                .filter(numberField -> !data.containsKey(numberField))
                .filter(numberField -> Objects.nonNull(numbers.getString(numberField)))
                .forEach(numberField -> {
                    final String code = numbers.getString(numberField);
                    numberMap.put(numberField, stub.indent(code, sigma));
                });
            /* Combine number map here for generation */
            return Ux.thenCombine(numberMap).compose(generated -> {
                generated.forEach(data::put);
                return Ux.future(data);
            });
        }));
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        /* Compress all sigma no value */
        final String sigma = Ut.valueString(data, KName.SIGMA);
        if (Ut.isNil(sigma) || Ut.isNil(data)) {
            return Ux.future(data);
        }
        /* Number generation */
        return this.run(data, in, (numbers) -> Ux.channelAsync(Indent.class, () -> Ux.future(data), stub -> {
            Ke.infoKe(LOGGER, "Table here {0}, Size {1}, Serial numbers {2}", in.module().getTable(), data.size(), numbers.encode());
            /* Queue<String> */
            final ConcurrentMap<String, Future<List<String>>> numberMap = new ConcurrentHashMap<>();
            numbers.fieldNames().stream()
                .filter(numberField -> Objects.nonNull(numbers.getString(numberField)))
                .forEach(numberField -> {
                    final String code = numbers.getString(numberField);
                    numberMap.put(numberField, stub.indent(code, sigma, data.size())
                        .compose(queue -> Ux.future(new ArrayList<>(queue))));
                });
            /* Combine */
            return Ux.thenCombine(numberMap).compose(generated -> {
                generated.forEach((numberField, numberList) -> {
                    final Queue<String> numberQueue = new LinkedBlockingDeque<>(numberList);
                    if (numberQueue.size() == data.size()) {
                        Ut.itJArray(data).forEach(json -> {
                            // This kind of situation may miss some numbers when you provide numbers
                            if (!json.containsKey(numberField)) {
                                json.put(numberField, numberQueue.poll());
                            }
                        });
                    }
                });
                return Ux.future(data);
            });
        }));
    }

    private <T> Future<T> run(final T data, final IxMod in, final Function<JsonObject, Future<T>> executor) {
        final KModule module = in.module();
        final KField field = module.getField();
        final JsonObject numbers = field.getNumbers();
        if (Ut.isNil(numbers)) {
            return Ux.future(data);
        } else {
            return executor.apply(numbers);
        }
    }
}
