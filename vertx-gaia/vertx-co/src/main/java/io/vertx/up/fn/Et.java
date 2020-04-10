package io.vertx.up.fn;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.ZeroException;

@SuppressWarnings("all")
class Et {

    static <T> void execZero(final JsonObject data, final ZeroBiConsumer<T, String> fnIt)
            throws ZeroException {
        for (final String name : data.fieldNames()) {
            final Object item = data.getValue(name);
            if (null != item) {
                fnIt.accept((T) item, name);
            }
        }
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param fnIt      iterator
     * @param <T>       element type
     */
    static <T> void execZero(final JsonArray dataArray, final ZeroBiConsumer<T, String> fnIt)
            throws ZeroException {
        execZero(dataArray, JsonObject.class, (element, index) -> execZero(element, fnIt));
    }

    /**
     * @param dataArray JsonArray that will be iterated
     * @param clazz     expected class
     * @param fnIt      iterator
     * @param <T>       element type T ( generic )
     * @throws ZeroException element zero here
     */
    static <T> void execZero(final JsonArray dataArray, final Class<T> clazz, final ZeroBiConsumer<T, Integer> fnIt)
            throws ZeroException {
        final int size = dataArray.size();
        for (int idx = Values.IDX; idx < size; idx++) {
            final Object value = dataArray.getValue(idx);
            if (null != value) {
                if (clazz == value.getClass()) {
                    final T item = (T) value;
                    fnIt.accept(item, idx);
                }
            }
        }
    }
}
