package io.vertx.up.plugin.neo4j;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.neo4j.refine.N4J;
import io.vertx.up.plugin.neo4j.sync.GraphicAnalyzer;
import io.vertx.up.plugin.neo4j.sync.N4JSession;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.Objects;

public class Neo4jClientImpl implements Neo4jClient {
    private static final Annal LOGGER = Annal.get(Neo4jClientImpl.class);
    private transient final Vertx vertx;

    /* Configuration */
    private transient Neo4jConfig config;
    private transient Driver driver;
    private transient String graph = "default";

    /* Component */
    private transient Neo4JSession session;
    private transient GraphicAnalyzer analyzer;

    Neo4jClientImpl(final Vertx vertx, final Neo4jConfig config) {
        this.vertx = vertx;
        this.config = config;
        this.initDatabase();
    }

    private void initDatabase() {
        final String uri = this.config.uri();
        LOGGER.info("[ ZERO ] Graphic database initialized: {0}", uri);
        this.driver = GraphDatabase.driver(uri, this.config.token(), this.config.config());
    }

    @Override
    public Neo4jClient init(final JsonObject params) {
        this.config = Neo4jConfig.create(params);
        this.initDatabase();
        return this;
    }

    @Override
    public Neo4jClient connect(final String graph) {
        if (Ut.isNotNil(graph)) {
            this.graph = graph;
        }
        /*
         * Session initialize
         */
        if (this.config.isAsync()) {
            // TODO: Async Session implementation
            // this.session = new N4JAsyncSession(graph).bind(this.driver);
        } else {
            this.session = new N4JSession(this.graph);
            this.session.bind(this.driver).constraints();
            // Analyzer
            this.analyzer = GraphicAnalyzer.create(this.graph, this.driver);
        }
        return this;
    }

    @Override
    public boolean connected() {
        try {
            this.driver.verifyConnectivity();
            return true;
        } catch (final Throwable ex) {
            return false;
        }
    }

    @Override
    public Future<JsonObject> nodeCreate(final JsonObject node) {
        return this.session.create(node);
    }

    @Override
    public Future<JsonArray> nodeCreate(final JsonArray nodes) {
        return this.session.create(nodes);
    }

    @Override
    public Future<JsonObject> nodeUpdate(final JsonObject node) {
        return this.session.update(node);
    }

    @Override
    public Future<JsonArray> nodeUpdate(final JsonArray nodes) {
        return this.session.update(nodes);
    }

    @Override
    public Future<JsonObject> nodeRemove(final JsonObject node) {
        return this.session.delete(node);
    }

    @Override
    public Future<JsonArray> nodeRemove(final JsonArray nodes) {
        return this.session.delete(nodes);
    }

    @Override
    public Future<JsonObject> nodeFind(final String key) {
        return this.session.find(new JsonObject().put("key", key));
    }

    @Override
    public boolean nodeExisting(final String key) {
        return Ut.isNotNil(this.session.findSync(new JsonObject().put("key", key)));
    }

    @Override
    public Future<JsonObject> edgeCreate(final JsonObject edge) {
        return this.session.link(edge);
    }

    @Override
    public Future<JsonArray> edgeCreate(final JsonArray edge) {
        return this.session.link(edge);
    }

    @Override
    public Future<JsonObject> edgeUpdate(final JsonObject edge) {
        return this.session.relink(edge);
    }

    @Override
    public Future<JsonArray> edgeUpdate(final JsonArray edge) {
        return this.session.relink(edge);
    }

    @Override
    public Future<JsonObject> edgeRemove(final JsonObject edge) {
        return this.session.unlink(edge);
    }

    @Override
    public Future<JsonArray> edgeRemove(final JsonArray edge) {
        return this.session.unlink(edge);
    }

    @Override
    public Future<JsonObject> graphic(final JsonObject node) {
        return this.graphic(node, -1);
    }

    @Override
    public Future<JsonObject> graphic(final JsonObject node, final Integer level) {
        if (Objects.isNull(node)) {
            return Ux.future(N4J.graphicDefault());
        } else {
            N4J.LOG.Node.info(this.getClass(), "Node found: {0}", node.encode());
            return this.analyzer.searchAsync(node, level);
        }
    }

    @Override
    public Future<JsonObject> graphicByKey(final String key) {
        return this.nodeFind(key).compose(this::graphic);
    }

    @Override
    public Future<JsonObject> graphicByKey(final String key, final Integer level) {
        return this.nodeFind(key).compose(node -> this.graphic(node, level));
    }

    @Override
    public Future<Boolean> graphicReset() {
        return this.session.reset();
    }
}
