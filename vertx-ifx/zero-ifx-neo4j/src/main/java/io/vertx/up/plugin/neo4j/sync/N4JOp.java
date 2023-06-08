package io.vertx.up.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * Basic operations
 */
public interface N4JOp {
    String ALIAS_CREATE = "created";
    String ALIAS_UPDATE = "updated";
    String ALIAS_DELETE = "deleted";

    Future<JsonObject> create(JsonObject node);

    Future<JsonArray> create(JsonArray nodes);

    Future<JsonObject> update(JsonObject node);

    Future<JsonArray> update(JsonArray nodes);

    Future<JsonObject> delete(JsonObject node);

    Future<JsonArray> delete(JsonArray nodes);
}
