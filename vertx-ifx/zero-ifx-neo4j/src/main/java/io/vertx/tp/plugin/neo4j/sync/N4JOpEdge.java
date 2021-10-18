package io.vertx.tp.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.neo4j.refine.N4J;
import io.vertx.up.util.Ut;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.util.Objects;

public class N4JOpEdge extends AbstractN4JExecutor implements N4JOp {
    private final transient String graph;
    private final transient Driver driver;

    N4JOpEdge(final String graph, final Driver driver) {
        this.graph = graph;
        this.driver = driver;
    }

    @Override
    public Future<JsonObject> create(final JsonObject node) {
        return this.doAsync(node, processed -> N4J.edgeFind(this.graph, processed)).compose(result -> {
            if (Ut.isNil(result)) {
                return this.doAsync(node, processed -> N4J.edgeAdd(this.graph, processed));
            } else {
                N4J.warnEdge(this.getClass(), "Existing of {0}, Skip", node.encode());
                return Future.succeededFuture(node);
            }
        });
    }

    @Override
    public Future<JsonArray> create(final JsonArray nodes) {
        return this.doAsync(nodes, processed -> N4J.edgeFind(this.graph, nodes)).compose(result -> {
            final JsonArray added = new JsonArray();
            final JsonArray ignored = new JsonArray();
            for (int idx = 0; idx < nodes.size(); idx++) {
                final JsonObject item = result.getJsonObject(idx);
                if (Objects.isNull(item)) {
                    added.add(nodes.getValue(idx));
                } else {
                    ignored.add(nodes.getValue(idx));
                }
            }
            if (!ignored.isEmpty()) {
                N4J.warnEdge(this.getClass(), "Existing of {0}, Skip", ignored.encode());
            }
            if (added.isEmpty()) {
                return Future.succeededFuture(nodes);
            } else {
                return this.doAsync(added, processed -> N4J.edgeAdd(this.graph, processed))
                    .compose(created -> Future.succeededFuture(nodes));
            }
        });
    }

    @Override
    public Future<JsonObject> update(final JsonObject node) {
        return this.doAsync(node, processed -> N4J.edgeDelete(this.graph, processed))
            .compose(nil -> this.doAsync(node, processed -> N4J.edgeAdd(this.graph, processed)));
    }

    @Override
    public Future<JsonArray> update(final JsonArray nodes) {
        return this.doAsync(nodes, processed -> N4J.edgeDelete(this.graph, processed))
            .compose(nil -> this.doAsync(nodes, processed -> N4J.edgeAdd(this.graph, processed)));
    }

    @Override
    public Future<JsonObject> delete(final JsonObject node) {
        return this.doAsync(node, processed -> N4J.edgeDelete(this.graph, processed));
    }

    @Override
    public Future<JsonArray> delete(final JsonArray nodes) {
        return this.doAsync(nodes, processed -> N4J.edgeDelete(this.graph, processed));
    }

    protected Session session() {
        return this.driver.session();
    }
}
