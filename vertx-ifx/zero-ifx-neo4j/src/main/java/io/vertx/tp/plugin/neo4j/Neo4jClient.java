package io.vertx.tp.plugin.neo4j;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.TpClient;

public interface Neo4jClient extends TpClient<Neo4jClient> {

    static Neo4jClient createShared(final Vertx vertx) {
        return new Neo4jClientImpl(vertx, Neo4jConfig.create());
    }

    static Neo4jClient createShared(final Vertx vertx, final JsonObject config) {
        return new Neo4jClientImpl(vertx, Neo4jConfig.create(config));
    }

    @Fluent
    @Override
    Neo4jClient init(JsonObject params);

    /*
     * Because neo4j do not support multi graph here
     * We'll use label as prefix for different graphics
     */
    @Fluent
    Neo4jClient connect(String graph);

    /*
     * Node basic operations
     * {
     *    "key": "id for current node",
     *    "code": "secondary id for current node",
     *    "x": "x position",
     *    "y": "y position",
     *    "data": {
     *         "comment": "Data Information of current node ( Business Info )"
     *    }
     *    "...": "Other properties for current node"
     * }
     * Operation List:
     * 1) create
     * 2) update ( By unique )
     * 3) remove ( By unique )
     * 4) find
     *    - by code
     *    - by key
     */
    Future<JsonObject> nodeCreate(JsonObject node);

    Future<JsonArray> nodeCreate(JsonArray nodes);

    Future<JsonObject> nodeUpdate(JsonObject node);

    Future<JsonArray> nodeUpdate(JsonArray nodes);

    Future<JsonObject> nodeRemove(JsonObject node);

    Future<JsonArray> nodeRemove(JsonArray nodes);

    Future<JsonObject> nodeFind(String key);

    boolean nodeExisting(String key);

    /*
     * Edge basic operations
     * {
     *     "key": "id for current edge",
     *     "source": "key / code of node",
     *     "target": "key / code of node",
     *     "type": "edge type information",
     *     "data": {
     *          "comment": "Data Information"
     *     }
     * }
     * Operation List:
     * 1) create
     * 2) update ( By unique )
     * 3) remove ( By unique )
     */
    Future<JsonObject> edgeCreate(JsonObject edge);

    Future<JsonArray> edgeCreate(JsonArray edge);

    Future<JsonObject> edgeUpdate(JsonObject edge);

    Future<JsonArray> edgeUpdate(JsonArray edge);

    Future<JsonObject> edgeRemove(JsonObject edge);

    Future<JsonArray> edgeRemove(JsonArray edge);

    /*
     * {
     *     "nodes": [],
     *     "edges": []
     * }
     */
    Future<JsonObject> graphic(JsonObject node);

    Future<JsonObject> graphic(JsonObject node, Integer level);

    Future<JsonObject> graphicByKey(String key);

    Future<JsonObject> graphicByKey(String key, Integer level);
}
