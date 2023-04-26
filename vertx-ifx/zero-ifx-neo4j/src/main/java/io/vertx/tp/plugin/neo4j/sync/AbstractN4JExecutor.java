package io.vertx.tp.plugin.neo4j.sync;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.neo4j.driver.Session;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.vertx.tp.plugin.neo4j.refine.N4J.LOG;

public abstract class AbstractN4JExecutor {

    protected JsonObject doSync(final JsonObject processed, final String alias, final Function<JsonObject, String> consumer) {
        final String command = consumer.apply(processed);
        LOG.Node.info(this.getClass(), "Alias: {1}, Command: {0}", command, alias);

        final N4JExecutor executor = N4JExecutor.create().bind(this.session());
        return executor.execute(command, processed, alias);
    }

    protected JsonArray doSync(final JsonArray processed, final Function<JsonArray, List<String>> consumer) {
        final List<String> command = consumer.apply(processed);
        LOG.Edge.debug(this.getClass(), "Command: {0}", command);
        final N4JExecutor executor = N4JExecutor.create().bind(this.session());
        return executor.execute(command, processed);
    }

    private JsonObject doSync(final JsonObject processed, final Function<JsonObject, String> consumer) {
        final String command = consumer.apply(processed);
        LOG.Edge.debug(this.getClass(), "Command: {0}", command);

        final N4JExecutor executor = N4JExecutor.create().bind(this.session());
        return executor.execute(command, processed);
    }

    private JsonArray doSync(final JsonArray processed, final String alias, final Function<JsonArray, List<String>> consumer) {
        final List<String> commands = consumer.apply(processed);
        LOG.Node.info(this.getClass(), "Alias: {1}, Size: {0}", commands.size(), alias);

        final N4JExecutor executor = N4JExecutor.create().bind(this.session());
        return executor.execute(commands, processed, alias);
    }


    protected Future<JsonArray> doAsync(final JsonArray processed, final Function<JsonArray, List<String>> consumer) {
        return Future.succeededFuture(this.doSync(processed, consumer));
    }

    protected Future<JsonObject> doAsync(final JsonObject processed, final Function<JsonObject, String> consumer) {
        return Future.succeededFuture(this.doSync(processed, consumer));
    }

    protected Future<JsonObject> doAsync(final JsonObject processed, final String alias, final Function<JsonObject, String> consumer) {
        return Future.succeededFuture(this.doSync(processed, alias, consumer));
    }

    protected Future<JsonArray> doAsync(final JsonArray processed, final String alias, final Function<JsonArray, List<String>> consumer) {
        return Future.succeededFuture(this.doSync(processed, alias, consumer));
    }

    protected void execute(final Supplier<List<String>> supplier) {
        final List<String> commands = supplier.get();
        final N4JExecutor executor = N4JExecutor.create().bind(this.session());
        executor.execute(commands);
    }

    protected abstract Session session();
}
