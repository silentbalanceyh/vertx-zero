package io.vertx.up.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.neo4j.refine.N4J;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

public class N4JOpNode extends AbstractN4JExecutor implements N4JOp {
    private final transient String graph;
    private final transient Driver driver;

    N4JOpNode(final String graph, final Driver driver) {
        this.graph = graph;
        this.driver = driver;
    }

    @Override
    public Future<JsonObject> create(final JsonObject node) {
        return this.doAsync(N4J.nodeMarker(node), ALIAS_CREATE,
            /* Command Supplier */
            processed -> N4J.nodeAdd(this.graph, processed, ALIAS_CREATE));
    }

    @Override
    public Future<JsonArray> create(final JsonArray nodes) {
        return this.doAsync(N4J.nodeMarker(nodes), ALIAS_CREATE,
            /* Command Supplier */
            processed -> N4J.nodeAdd(this.graph, processed, ALIAS_CREATE));
    }

    @Override
    public Future<JsonObject> update(final JsonObject node) {

        return this.doAsync(N4J.nodeMarker(node), ALIAS_UPDATE,
            /* Command Supplier */
            processed -> N4J.nodeUpdate(this.graph, this.unique(node), processed, ALIAS_UPDATE));
    }

    @Override
    public Future<JsonArray> update(final JsonArray nodes) {

        return this.doAsync(N4J.nodeMarker(nodes), ALIAS_UPDATE,
            /* Command Supplier */
            processed -> N4J.nodeUpdate(this.graph, this.unique(nodes), processed, ALIAS_UPDATE));
    }

    @Override
    public Future<JsonObject> delete(final JsonObject node) {
        return this.doAsync(N4J.nodeUnique(node), ALIAS_DELETE,
            /* Command Supplier */
            processed -> N4J.nodeDelete(this.graph, processed, ALIAS_DELETE)
        ).compose(nil -> Future.succeededFuture(node));
    }

    @Override
    public Future<JsonArray> delete(final JsonArray nodes) {
        return this.doAsync(N4J.nodeUnique(nodes), ALIAS_DELETE,
            /* Command Supplier */
            processed -> N4J.nodeDelete(this.graph, processed, ALIAS_DELETE)
        ).compose(nil -> Future.succeededFuture(nodes));
    }

    private JsonObject unique(final JsonObject data) {
        /* Condition */
        final JsonObject condition = N4J.nodeUnique(data);
        N4J.LOG.Node.info(this.getClass(), "Cond: {0}", condition.encode());
        return condition;
    }

    private JsonArray unique(final JsonArray data) {
        /* Condition */
        final JsonArray condition = N4J.nodeUnique(data);
        N4J.LOG.Node.info(this.getClass(), "Cond: {0}", condition.encode());
        return condition;
    }

    @Override
    protected Session session() {
        return this.driver.session();
    }
}
