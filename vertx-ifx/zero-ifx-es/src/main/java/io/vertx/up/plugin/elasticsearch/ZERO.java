package io.vertx.up.plugin.elasticsearch;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

interface Pool {

    ConcurrentMap<ChangeFlag, ConcurrentMap<String, EsAmbit>> ES_CACHE =
        new ConcurrentHashMap<ChangeFlag, ConcurrentMap<String, EsAmbit>>() {
            {
                this.put(ChangeFlag.ADD, new ConcurrentHashMap<>());
                this.put(ChangeFlag.UPDATE, new ConcurrentHashMap<>());
                this.put(ChangeFlag.DELETE, new ConcurrentHashMap<>());
            }
        };

    ConcurrentMap<ChangeFlag, BiFunction<String, JsonObject, EsAmbit>> ES_FUN =
        new ConcurrentHashMap<ChangeFlag, BiFunction<String, JsonObject, EsAmbit>>() {
            {
                this.put(ChangeFlag.ADD, EsAmbitAdd::new);
                this.put(ChangeFlag.UPDATE, EsAmbitUpdate::new);
                this.put(ChangeFlag.DELETE, EsAmbitDelete::new);
            }
        };
}
