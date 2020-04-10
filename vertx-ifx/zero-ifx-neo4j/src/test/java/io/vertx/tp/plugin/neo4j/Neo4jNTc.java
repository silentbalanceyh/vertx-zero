package io.vertx.tp.plugin.neo4j;

import io.vertx.ext.unit.TestContext;
import org.junit.Ignore;
import org.junit.Test;

/*
 * When debug, please remove @Ignore annotation
 */
@Ignore
public class Neo4jNTc extends Neo4jQuiz {

    // MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r
    @Test
    public void testAddJ(final TestContext context) {
        this.executeA("case1-array.json", Neo4jClient::nodeCreate);
    }

    @Test
    public void testAddA(final TestContext context) {
        this.executeJ("case1-single.json", Neo4jClient::nodeCreate);
    }

    @Test
    public void testUpdate(final TestContext context) {
        this.executeJ("case2-object.json", Neo4jClient::nodeUpdate);
    }

    @Test
    public void testUpdate1(final TestContext context) {
        this.executeJ("case2-object-key.json", Neo4jClient::nodeUpdate);
    }

    @Test
    public void testDeleteJ(final TestContext context) {
        this.executeJ("case3-delete.json", Neo4jClient::nodeRemove);
    }

    @Test
    public void testDeleteA(final TestContext context) {
        this.executeA("case3-array.json", Neo4jClient::nodeRemove);
    }
}
