package io.mature.extension.uca.elasticsearch;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/*
 *
 */
public interface EsIndex {

    static EsIndex create(final ChangeFlag type, final String identifier) {
        final Function<String, EsIndex> executor = Pool.POOL_INDEX_SUPPLIER.get(type);
        final ConcurrentMap<String, EsIndex> pool = Pool.POOL_INDEX.get(type);
        if (Objects.isNull(pool)) {
            return executor.apply(identifier);
        } else {
            return Cc.pool(pool, identifier, () -> executor.apply(identifier));
        }
    }


    Future<JsonObject> indexAsync(JsonObject record);

    Future<JsonArray> indexAsync(JsonArray record);
}

interface Pool {

    ConcurrentMap<ChangeFlag, ConcurrentHashMap<String, EsIndex>> POOL_INDEX = new ConcurrentHashMap<ChangeFlag, ConcurrentHashMap<String, EsIndex>>() {
        {
            this.put(ChangeFlag.ADD, new ConcurrentHashMap<>());
            this.put(ChangeFlag.UPDATE, new ConcurrentHashMap<>());
            this.put(ChangeFlag.DELETE, new ConcurrentHashMap<>());
        }
    };

    ConcurrentMap<ChangeFlag, Function<String, EsIndex>> POOL_INDEX_SUPPLIER = new ConcurrentHashMap<ChangeFlag, Function<String, EsIndex>>() {
        {
            this.put(ChangeFlag.ADD, EsAddIndexer::new);
            this.put(ChangeFlag.UPDATE, EsUpdateIndexer::new);
            this.put(ChangeFlag.DELETE, EsDeleteIndexer::new);
        }
    };
}