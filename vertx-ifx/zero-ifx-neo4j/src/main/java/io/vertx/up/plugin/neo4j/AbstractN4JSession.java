package io.vertx.up.plugin.neo4j;

import io.vertx.up.plugin.neo4j.sync.AbstractN4JExecutor;
import org.neo4j.driver.Driver;

public abstract class AbstractN4JSession extends AbstractN4JExecutor implements Neo4JSession {
    protected transient Driver driver;
    protected transient String graph;

    protected AbstractN4JSession(final String graph) {
        this.graph = graph;
    }

    @Override
    public Neo4JSession bind(final Driver driver) {
        this.driver = driver;
        return this;
    }
}
