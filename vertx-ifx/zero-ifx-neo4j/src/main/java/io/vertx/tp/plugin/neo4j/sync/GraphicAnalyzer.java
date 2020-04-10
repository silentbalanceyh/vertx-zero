package io.vertx.tp.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.neo4j.refine.N4J;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;

import java.util.HashSet;
import java.util.Set;

public class GraphicAnalyzer extends AbstractN4JExecutor {
    private final transient String graph;
    private final transient Driver driver;

    private GraphicAnalyzer(final String graph, final Driver driver) {
        this.graph = graph;
        this.driver = driver;
    }

    public static GraphicAnalyzer create(final String graph, final Driver driver) {
        return new GraphicAnalyzer(graph, driver);
    }

    public Future<JsonObject> searchAsync(final JsonObject node, final Integer integer) {
        final String command = N4J.graphicByKey(this.graph, integer);
        N4J.infoEdge(this.getClass(), "Command: {0}, Args: {1}", command, node.encode());
        final Session session = this.session();
        final Transaction transaction = session.beginTransaction();
        final Result result = transaction.run(command, N4J.parameters(node));
        final JsonObject response = new JsonObject();
        final JsonArray nodes = new JsonArray();
        final JsonArray edges = new JsonArray();
        final Set<String> duplicated = new HashSet<>();
        result.stream().forEach(record -> {
            /*
             * From nodes
             */
            final JsonObject from = N4J.toJson(record.get("from"));
            if (!duplicated.contains(from.getString("key"))) {
                nodes.add(from);
                duplicated.add(from.getString("key"));
            }
            /*
             * To nodes
             */
            final JsonObject to = N4J.toJson(record.get("to"));
            if (!duplicated.contains(to.getString("key"))) {
                nodes.add(to);
                duplicated.add(to.getString("key"));
            }
            /*
             * Edges
             */
            edges.add(N4J.toJson(record.get("edge")));
        });
        response.put("nodes", nodes);
        response.put("edges", edges);
        transaction.commit();
        /* Must close */
        transaction.close();
        session.close();
        return Future.succeededFuture(response);
    }

    @Override
    protected Session session() {
        return this.driver.session();
    }
}
