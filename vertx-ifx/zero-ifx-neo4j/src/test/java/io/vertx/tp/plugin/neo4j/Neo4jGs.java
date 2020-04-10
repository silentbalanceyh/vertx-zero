package io.vertx.tp.plugin.neo4j;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;

import java.io.Serializable;
import java.util.Objects;

public class Neo4jGs implements Serializable {

    private final transient String name;

    private final transient JsonArray nodes;

    private final transient JsonArray edges;

    public Neo4jGs(final JsonObject input) {
        this.name = input.getString("name");
        this.nodes = input.getJsonArray("nodes");
        this.edges = input.getJsonArray("edges");
    }

    public String getName() {
        return this.name;
    }

    public JsonArray getNodes() {
        return Objects.isNull(this.nodes) ? new JsonArray() : this.nodes;
    }

    public JsonObject getNode() {
        return Objects.isNull(this.nodes) ? new JsonObject() : this.nodes.getJsonObject(Values.IDX);
    }

    public JsonArray getEdges() {
        return Objects.isNull(this.edges) ? new JsonArray() : this.edges;
    }

    public JsonObject getEdge() {
        return Objects.isNull(this.edges) ? new JsonObject() : this.edges.getJsonObject(Values.IDX);
    }

    @Override
    public String toString() {
        return "Neo4jGs{" +
                "name='" + this.name + '\'' +
                ", nodes=" + this.nodes +
                ", edges=" + this.edges +
                '}';
    }
}
