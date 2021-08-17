package io.vertx.tp.modular.jooq.internal;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataEvent;

import java.util.function.Function;

@SuppressWarnings("unchecked")
class Event {

    static <T> T bool(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        /* 打印模板 */
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return (T) response.succeed();
    }

    static <T> T record(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor,
            final boolean isArray
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        if (isArray) {
            return (T) response.dataA();
        } else {
            return (T) response.dataR();
        }
    }

    static Long count(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return response.getCounter();
    }

    static JsonObject pagination(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return response.dataP();
    }

    // ----------------------- Async ----------------------
    static <T> Future<T> recordAsync(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor,
            final boolean isArray
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        if (isArray) {
            return (Future<T>) response.dataAAsync();
        } else {
            return (Future<T>) response.dataRAsync();
        }
    }

    static Future<JsonObject> paginationAsync(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return response.dataPAsync();
    }
}
