package io.vertx.mod.plugin.neo4j;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.unit.TestContext;
import io.vertx.up.plugin.neo4j.Neo4jClient;
import io.vertx.up.plugin.neo4j.Neo4jInfix;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Neo4jGTc extends Neo4jQuiz {
    @Test
    public void testReady(final TestContext context) {
        final Neo4jGs gs = this.create("case5-graphic-test.json");
        final Neo4jClient client = Neo4jInfix.getClient().connect(gs.getName());
        // client.edgeCreate(gs.getEdges());
        // client.nodeCreate(gs.getNodes()).compose(created -> client.edgeCreate(gs.getEdges())).compose(client::closeAsync);
    }

    @Test
    public void testAnalyze(final TestContext context) {
        final Neo4jGs gs = this.create("case5-graphic.json");
        final Neo4jClient client = Neo4jInfix.getClient().connect(gs.getName());
        this.tcAsync(context, client.graphicByKey("442dc45cb85a6345b1fab405bfbb5fa9"), actual -> {
            final JsonArray nodes = actual.getJsonArray("nodes");
            final JsonArray edges = actual.getJsonArray("edges");
            System.err.println(nodes.size() + "," + edges.size() + "," + actual.encodePrettily());
        });
    }

    @Test
    public void testPurge(final TestContext context) {
        final Neo4jGs graphic = this.create("case1-array.json");
        final Neo4jClient client = Neo4jInfix.getClient().connect(graphic.getName());
        this.tcAsync(context, client.graphicReset(), actual -> {
            System.out.println("The graphic " + graphic.getName() + " has been reset: " + actual);
        });
    }
}
