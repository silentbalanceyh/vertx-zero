package io.vertx.mod.crud.uca.next;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.ke.refine.Ke;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class OkAExport implements Co<JsonObject, JsonArray, JsonArray, JsonArray> {

    private final transient List<String> columns = new ArrayList<>();

    OkAExport(final List<String> columns) {
        this.columns.addAll(columns);
    }

    @Override
    public Future<JsonArray> ok(final JsonArray data, final JsonArray columns) {
        /*
         * Column initialization
         */
        final ConcurrentMap<String, String> headers = new ConcurrentHashMap<>();
        columns.stream().map(Ix::onColumn).filter(Objects::nonNull).forEach(kv -> {
            /* Calculated */
            if (this.columns.contains(kv.key())) {
                headers.put(kv.key(), kv.value());
            }
        });
        return Ke.combineAsync(data, headers, this.columns);
    }
}
