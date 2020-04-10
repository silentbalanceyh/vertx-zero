package io.vertx.tp.ke.tunnel;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._501JooqReferenceException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.jq.UxJooq;

import java.util.Objects;
import java.util.Set;

/*
 * Nexus implementation by specific definition here.
 * You must set UxJooq instead of other Dao here.
 * 1) This interface is for `modelKey` and `modelId` only
 * 2) A Table defined `modelKey` and `modelId`, the B table
 * 3) When relation has been changed from B, the `modelId` and `modelKey` must be updated
 */
public interface Nexus {

    static Nexus create(final Class<?> clazz) {
        Fn.out(Objects.isNull(clazz), _501JooqReferenceException.class, NexusLinker.class);
        return new NexusLinker(clazz);
    }

    /*
     * UxJooq processing for different actions
     */
    Nexus on(UxJooq jooq);

    /*
     * `identifier` means model id refer to unique identifier of each App
     * `key` means model key refer to unique
     * {
     *     "identifier": "model id, this model is bind to UxJooq",
     *     "key": "record id",
     *     "sigma": "related nexus that bind to app sigma"
     * }
     */
    Future<JsonObject> fetchNexus(JsonObject filters);

    /*
     * `key` is the entity primary key
     * This method will update nexues between
     * Host record and Slave record here.
     * 1) Get record by `key`
     * 2) Provider params such as
     * {
     *     "identifier": "model id",
     *     "key": "record id"
     * }
     */
    Future<JsonObject> updateNexus(String key, JsonObject params);

    /*
     * 1) Get records by ke set
     * 2) You can ignore other condition
     */
    Future<JsonArray> fetchNexus(Set<String> keys);
}
