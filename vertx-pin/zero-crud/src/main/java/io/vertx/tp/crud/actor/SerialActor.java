package io.vertx.tp.crud.actor;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExSerial;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class SerialActor extends AbstractActor {
    private static final Annal LOGGER = Annal.get(SerialActor.class);

    @Override
    public JsonObject proc(final JsonObject data, final IxModule config) {
        throw new RuntimeException("Do not support this method here.");
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject data, final IxModule config) {
        final KField field = config.getField();
        final JsonObject numbers = field.getNumbers();
        if (Ut.isNil(numbers)) {
            /*
             * Do not generate numbers
             */
            return Ux.future(data);
        } else {
            /*
             * Generate numbers here
             */
            Ke.infoKe(LOGGER, "Table here {0}, Serial numbers {0}", config.getTable(), numbers.encodePrettily());
            return Ke.channelAsync(ExSerial.class,
                    () -> Ux.future(data),
                    serial -> {
                        final String sigma = data.getString(io.vertx.tp.ke.cv.KeField.SIGMA);
                        if (Ut.isNil(sigma)) {
                            return Ux.future(data);
                        } else {
                            final ConcurrentMap<String, Future<String>> numberMap =
                                    new ConcurrentHashMap<>();
                            numbers.fieldNames().stream()
                                    .filter(numberField -> !data.containsKey(numberField))
                                    .filter(numberField -> Objects.nonNull(numbers.getString(numberField)))
                                    .forEach(numberField -> {
                                        final String code = numbers.getString(numberField);
                                        numberMap.put(numberField, serial.serial(sigma, code));
                                    });
                            /*
                             * Future combine
                             */
                            return Ux.thenCombine(numberMap)
                                    /*
                                     * Combine number map here for generation
                                     * 1) Current should be `account-item` instead of others
                                     */
                                    .compose(generated -> {
                                        final Set<String> generatedFields = generated.keySet();
                                        generatedFields.forEach(generatedField -> data.put(generatedField, generated.get(generatedField)));
                                        return Ux.future(data);
                                    });
                        }
                    });
        }
    }

}
