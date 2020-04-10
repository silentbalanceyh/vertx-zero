package io.vertx.tp.plugin.neo4j;

import io.vertx.ext.unit.TestContext;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Neo4jRTc extends Neo4jQuiz {
    @Test
    public void testAddJ(final TestContext context) {
        final Neo4jGs gs = this.create("case4-object.json");
        final Neo4jClient client = Neo4jInfix.getClient().connect(gs.getName());
        // client.nodeCreate(gs.getNodes());
        client.edgeCreate(gs.getEdge()).compose(this::resultAsync);
        // client.nodeCreate(gs.getNodes()).compose(created -> client.edgeCreate(gs.getEdges()));
    }

    @Test
    public void testAddA(final TestContext context) {
        final Neo4jGs gs = this.create("case4-array.json");
        final Neo4jClient client = Neo4jInfix.getClient().connect(gs.getName());
        // client.nodeCreate(gs.getNodes());
        client.edgeCreate(gs.getEdges()).compose(this::resultAsync);
    }
}
