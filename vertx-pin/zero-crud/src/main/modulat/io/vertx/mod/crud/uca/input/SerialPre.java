package io.vertx.mod.crud.uca.input;

import io.aeon.experiment.specification.KModule;
import io.horizon.spi.modeler.Indent;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vertx.mod.ke.refine.Ke.LOG;

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
        return this.run(data, in, (numbers) -> Ux.channelA(Indent.class, () -> Ux.future(data), stub -> {
            LOG.Ke.info(LOGGER, "Table here {0}, Serial numbers {1}", in.module().getTable(), numbers.encode());
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
            return Fn.combineM(numberMap).compose(generated -> {
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
        return this.run(data, in, (numbers) -> Ux.channelA(Indent.class, () -> Ux.future(data), stub -> {
            LOG.Ke.info(LOGGER, "Table here {0}, Size {1}, Serial numbers {2}", in.module().getTable(), data.size(), numbers.encode());
            /* Queue<String> */
            final ConcurrentMap<String, Future<Queue<String>>> numberMap = new ConcurrentHashMap<>();
            numbers.fieldNames().stream()
                .filter(numberField -> Objects.nonNull(numbers.getString(numberField)))
                .forEach(numberField -> numberMap.put(numberField, this.runIndent(data, numberField, size -> {
                    final String code = numbers.getString(numberField);
                    return stub.indent(code, sigma, size);
                })));
            /* Combine */
            return Fn.combineM(numberMap).compose(generated -> {
                generated.forEach((numberField, numberQueue) -> this.runFill(data, numberField, numberQueue));
                return Ux.future(data);
            });
        }));
    }

    private void runFill(final JsonArray source, final String field, final Queue<String> numberQueue) {
        if (numberQueue.isEmpty()) {
            return;
        }
        Ut.itJArray(source).forEach(json -> {
            // This kind of situation may miss some numbers when you provide numbers
            if (!json.containsKey(field) && !numberQueue.isEmpty()) {
                json.put(field, numberQueue.poll());
            }
        });
    }

    private Future<Queue<String>> runIndent(final JsonArray source, final String field,
                                            final Function<Integer, Future<Queue<String>>> generator) {
        final Set<String> valueSet = Ut.itJArray(source)
            .filter(json -> !json.containsKey(field))
            .map(json -> json.getString(field))
            .filter(Objects::isNull)
            .collect(Collectors.toSet());
        final int size = valueSet.size();
        if (0 < size) {
            return generator.apply(size);
        } else {
            return Ux.future(new LinkedBlockingDeque<>());
        }
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
