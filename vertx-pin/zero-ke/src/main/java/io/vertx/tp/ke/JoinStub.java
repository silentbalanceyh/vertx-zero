package io.vertx.tp.ke;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.jooq.UxJooq;

/*
 * Join stub implementation by some specific definition
 * 1. Read related join information and joined into current.
 * 2. Save joined information.
 *
 * There are some fields that current object must required.
 * 1. Contains ( modelId / modelKey )
 * 2. modelId means related ( model ), refer to table.
 *    -- Dynamic Mode: M_MODEL identifier.
 *    -- Static Mode: Class Name of Dao Class . ( Dao Class )
 *    modelKey means related ( model ), refer to record id.
 */
public interface JoinStub {

    JoinStub on(UxJooq jooq);

    /*
     * Read joined related information.
     */
    Future<JsonObject> fetch(String modelId, String modelKey);

    /*
     * Save joined related information.
     */
    Future<JsonObject> save(String modelId, String modelKey,
                            JsonObject data);
}
