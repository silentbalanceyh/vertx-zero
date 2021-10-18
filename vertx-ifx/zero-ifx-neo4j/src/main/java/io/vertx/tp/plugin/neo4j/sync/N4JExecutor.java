package io.vertx.tp.plugin.neo4j.sync;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.neo4j.refine.N4J;
import io.vertx.up.util.Ut;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.exceptions.ClientException;

import java.util.List;

class N4JExecutor {
    private transient Session session;

    private N4JExecutor() {
    }

    static N4JExecutor create() {
        return new N4JExecutor();
    }

    N4JExecutor bind(final Session session) {
        this.session = session;
        return this;
    }

    /*
     * Execute
     */
    JsonObject execute(final String command, final JsonObject params, final String alias) {
        final Transaction transaction = this.session.beginTransaction();
        final JsonObject result = Ut.isNil(alias)
            ? N4J.toJson(transaction.run(command, N4J.parameters(params)))
            : N4J.toJson(transaction.run(command, N4J.parameters(params)), alias);
        transaction.commit();

        /* Must close */
        transaction.close();
        this.session.close();
        return result;
    }

    JsonObject execute(final String command, final JsonObject params) {
        return this.execute(command, params, null);
    }

    JsonArray execute(final List<String> command, final JsonArray params) {
        return this.execute(command, params, null);
    }

    JsonArray execute(final List<String> commands, final JsonArray params, final String alias) {
        final JsonArray response = new JsonArray();
        final Transaction transaction = this.session.beginTransaction();
        Ut.itList(commands, (command, index) -> {
            final JsonObject item = params.getJsonObject(index);
            final JsonObject result = Ut.isNil(alias)
                ? N4J.toJson(transaction.run(command, N4J.parameters(item)))
                : N4J.toJson(transaction.run(command, N4J.parameters(item)), alias);
            response.add(result);
        });
        transaction.commit();

        /* Must close */
        transaction.close();
        this.session.close();
        return response;
    }

    void execute(final List<String> commands) {
        try {
            final Transaction transaction = this.session.beginTransaction();
            commands.forEach(command -> {
                N4J.infoCql(N4JExecutor.class, "Each Command: {0}", command);
                transaction.run(command);
            });
            transaction.commit();

            /* Must close */
            transaction.close();
            this.session.close();
        } catch (final ClientException ex) {
            /*
             * Existing, ignored
             */
            N4J.warnCql(N4JExecutor.class, "ClientException: {0}", ex.getMessage());
        }
    }
}
