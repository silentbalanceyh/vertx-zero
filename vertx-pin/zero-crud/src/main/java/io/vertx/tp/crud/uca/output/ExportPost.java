package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ExportPost implements Post<JsonArray> {
    private final transient List<String> columnList = new ArrayList<>();

    ExportPost(final List<String> columnList) {
        this.columnList.addAll(columnList);
    }

    @Override
    public Future<JsonArray> outAsync(final Object dataInput, final Object columnsInput) {
        if (dataInput instanceof JsonArray && columnsInput instanceof JsonArray) {
            final JsonArray data = (JsonArray) dataInput;
            final JsonArray columns = (JsonArray) columnsInput;
            /*
             * Column initialization
             */
            final ConcurrentMap<String, String> headers = new ConcurrentHashMap<>();
            columns.stream().map(Ix::onColumn).filter(Objects::nonNull).forEach(kv -> {
                /* Calculated */
                if (!this.columnList.contains(kv.getKey())) {
                    headers.put(kv.getKey(), kv.getValue());
                }
            });
            return Ke.combineAsync(data, headers, this.columnList);
        } else {
            return Ux.futureA();
        }
    }
}
