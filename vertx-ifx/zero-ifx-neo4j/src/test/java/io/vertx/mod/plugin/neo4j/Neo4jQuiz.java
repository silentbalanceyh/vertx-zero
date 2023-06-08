package io.vertx.mod.plugin.neo4j;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.plugin.neo4j.Neo4jClient;
import io.vertx.up.plugin.neo4j.Neo4jInfix;
import io.vertx.up.unity.Ux;
import org.junit.Ignore;

import java.util.function.BiFunction;

@Ignore
public class Neo4jQuiz extends ZeroBase {
    static {
        Neo4jInfix.init(VERTX);
    }

    protected Future<JsonObject> resultAsync(final JsonObject response) {
        System.err.println(response.encodePrettily());
        return Ux.future();
    }

    protected Future<JsonArray> resultAsync(final JsonArray response) {
        System.err.println(response.encodePrettily());
        return Ux.future();
    }

    protected Neo4jGs create(final String filename) {
        final JsonObject input = this.ioJObject(filename);
        return new Neo4jGs(input);
    }

    protected Future<JsonObject> executeJ(final String filename,
                                          final BiFunction<Neo4jClient, JsonObject, Future<JsonObject>> executor) {
        final Neo4jGs graphic = this.create(filename);
        final Neo4jClient client = Neo4jInfix.getClient().connect(graphic.getName());
        return executor.apply(client, graphic.getNode()).compose(this::resultAsync);
    }

    protected Future<JsonArray> executeA(final String filename,
                                         final BiFunction<Neo4jClient, JsonArray, Future<JsonArray>> executor) {
        final Neo4jGs graphic = this.create(filename);
        final Neo4jClient client = Neo4jInfix.getClient().connect(graphic.getName());
        return executor.apply(client, graphic.getNodes()).compose(this::resultAsync);
    }
}
