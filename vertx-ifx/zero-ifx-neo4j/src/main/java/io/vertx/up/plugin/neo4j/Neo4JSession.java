package io.vertx.up.plugin.neo4j;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.neo4j.driver.Driver;

public interface Neo4JSession {

    String ALIAS_FOUND = "found";
    String ALIAS_CONSTRAINT = "constraints";

    Neo4JSession bind(Driver driver);

    void constraints();

    /*
     * Node processing
     */
    Future<JsonObject> create(JsonObject node);

    Future<JsonArray> create(JsonArray nodes);

    Future<JsonObject> update(JsonObject node);

    Future<JsonArray> update(JsonArray nodes);

    Future<JsonObject> find(JsonObject condition);

    JsonObject findSync(JsonObject condition);

    Future<JsonObject> delete(JsonObject node);

    Future<JsonArray> delete(JsonArray nodes);

    /*
     * Edge processing
     */
    Future<JsonObject> link(JsonObject edge);

    Future<JsonArray> link(JsonArray edges);

    Future<JsonObject> relink(JsonObject edge);

    Future<JsonArray> relink(JsonArray edges);

    Future<JsonObject> unlink(JsonObject edge);

    Future<JsonArray> unlink(JsonArray edges);

    /*
     * Graphic Reset
     * Remove all nodes / edges
     */
    Future<Boolean> reset();
}
