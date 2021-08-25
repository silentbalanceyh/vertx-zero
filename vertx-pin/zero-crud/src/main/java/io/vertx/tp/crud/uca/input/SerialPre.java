package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExSerial;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SerialPre implements Pre {
    private static final Annal LOGGER = Annal.get(SerialPre.class);

    @Override
    public Future<JsonObject> inAsync(final JsonObject data, final IxIn in) {
        /* Sigma Checking */
        final String sigma = data.getString(KName.SIGMA);
        if (Ut.notNil(sigma)) {
            return Ux.future(data);
        }
        /* Number generation */
        return this.run(data, in, (numbers) -> Ke.channelAsync(ExSerial.class, () -> Ux.future(data), stub -> {
            Ke.infoKe(LOGGER, "Table here {0}, Serial numbers {0}", in.module().getTable(), numbers.encode());
            /* Channel */
            final ConcurrentMap<String, Future<String>> numberMap = new ConcurrentHashMap<>();
            numbers.fieldNames().stream()
                    .filter(numberField -> !data.containsKey(numberField))
                    .filter(numberField -> Objects.nonNull(numbers.getString(numberField)))
                    .forEach(numberField -> {
                        final String code = numbers.getString(numberField);
                        numberMap.put(numberField, stub.serial(sigma, code));
                    });
            /*
             * Combine number map here for generation
             */
            return Ux.thenCombine(numberMap).compose(generated -> {
                generated.forEach(data::put);
                return Ux.future(data);
            });
        }));
    }

    private <T> Future<T> run(final T data, final IxIn in, final Function<JsonObject, Future<T>> executor) {
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
