package io.horizon.util.fn;

import io.horizon.eon.VValue;
import io.horizon.exception.ProgramException;
import io.horizon.fn.ProgramBiConsumer;
import io.horizon.util.HaS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023/4/28
 */
@SuppressWarnings("unchecked")
class HConsumer {
    private HConsumer() {
    }

    public static <T> void bugIt(final JsonObject inputJ,
                                 final ProgramBiConsumer<T, String> consumer) throws ProgramException {
        if (HaS.isNotNil(inputJ)) {
            for (final String name : inputJ.fieldNames()) {
                final Object value = inputJ.getValue(name);
                if (Objects.nonNull(value)) {
                    consumer.accept((T) value, name);
                }
            }
        }
    }

    public static <T> void bugIt(final JsonArray inputA, final Class<?> clazz,
                                 final ProgramBiConsumer<T, Integer> consumer) throws ProgramException {
        final int size = inputA.size();
        for (int idx = VValue.IDX; idx < size; idx++) {
            final Object value = inputA.getValue(idx);
            if (null != value) {
                if (clazz == value.getClass()) {
                    final T item = (T) value;
                    consumer.accept(item, idx);
                }
            }
        }
    }
}
