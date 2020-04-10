package io.vertx.tp.modular.jooq.internal;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataEvent;

import java.util.function.Function;

@SuppressWarnings("unchecked")
class Event {

    static <T> T onBoolean(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        /* 打印模板 */
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return (T) response.succeed();
    }

    static <T> T onRecord(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor,
            final boolean isArray
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        if (isArray) {
            return (T) response.getRecords();
        } else {
            return (T) response.getRecord();
        }
    }

    static Long onCount(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return response.getCounter();
    }

    static JsonObject onPagination(
            final DataEvent event,
            final Function<DataEvent, DataEvent> executor
    ) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        return response.getPagination();
    }
}
