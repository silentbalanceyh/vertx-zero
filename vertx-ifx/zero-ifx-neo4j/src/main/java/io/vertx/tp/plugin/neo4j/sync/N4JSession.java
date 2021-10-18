package io.vertx.tp.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.neo4j.AbstractN4JSession;
import io.vertx.tp.plugin.neo4j.refine.N4J;
import io.vertx.up.unity.Ux;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class N4JSession extends AbstractN4JSession {

    private transient N4JOp nodeOp;
    private transient N4JOp edgeOp;

    public N4JSession(final String graph) {
        super(graph);
    }

    private N4JOp nodeOp() {
        if (Objects.isNull(this.nodeOp)) {
            this.nodeOp = new N4JOpNode(this.graph, this.driver);
        }
        return this.nodeOp;
    }

    private N4JOp edgeOp() {
        if (Objects.isNull(this.edgeOp)) {
            this.edgeOp = new N4JOpEdge(this.graph, this.driver);
        }
        return this.edgeOp;
    }

    @Override
    public Future<JsonObject> create(final JsonObject node) {
        return this.nodeOp().create(node);
    }

    @Override
    public Future<JsonArray> create(final JsonArray nodes) {
        return this.nodeOp().create(nodes);
    }

    @Override
    public Future<JsonObject> update(final JsonObject node) {
        return this.nodeOp().update(node);
    }

    @Override
    public Future<JsonArray> update(final JsonArray nodes) {
        return this.nodeOp().update(nodes);
    }

    @Override
    public Future<JsonObject> delete(final JsonObject node) {
        return this.nodeOp().delete(node);
    }

    @Override
    public Future<JsonArray> delete(final JsonArray nodes) {
        return this.nodeOp().delete(nodes);
    }

    @Override
    public Future<JsonObject> find(final JsonObject condition) {
        return this.doAsync(condition, ALIAS_FOUND,/* Command Supplier */
            processed -> N4J.nodeFind(this.graph, processed, ALIAS_FOUND));
    }

    @Override
    public JsonObject findSync(final JsonObject condition) {
        return this.doSync(condition, ALIAS_FOUND,
            processed -> N4J.nodeFind(this.graph, processed, ALIAS_FOUND));
    }

    @Override
    public Future<JsonObject> link(final JsonObject edge) {
        return this.edgeOp().create(edge);
    }

    @Override
    public Future<JsonArray> link(final JsonArray edges) {
        return this.edgeOp().create(edges);
    }

    @Override
    public Future<JsonObject> relink(final JsonObject edge) {
        return this.edgeOp().update(edge);
    }

    @Override
    public Future<JsonArray> relink(final JsonArray edges) {
        return this.edgeOp().update(edges);
    }

    @Override
    public Future<JsonObject> unlink(final JsonObject edge) {
        return this.edgeOp().delete(edge);
    }

    @Override
    public Future<JsonArray> unlink(final JsonArray edges) {
        return this.edgeOp().delete(edges);
    }

    @Override
    public void constraints() {
        /*
         * Default constraint processing,
         * 1) `key` must be unique
         */
        this.execute(() -> N4J.constraint(this.graph, new HashSet<String>() {
            {
                this.add("key");
            }
        }, ALIAS_CONSTRAINT));
    }

    @Override
    public Future<Boolean> reset() {
        this.execute(() -> {
            final List<String> commands = new ArrayList<>();
            commands.add(N4J.graphicReset(this.graph));
            return commands;
        });
        return Ux.future(Boolean.TRUE);
    }

    protected Session session() {
        return this.driver.session();
    }
}
