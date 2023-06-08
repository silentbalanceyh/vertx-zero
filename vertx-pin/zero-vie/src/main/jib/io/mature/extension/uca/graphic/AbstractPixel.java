package io.mature.extension.uca.graphic;

import io.horizon.uca.log.Annal;
import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.up.eon.KWeb;
import io.vertx.up.plugin.neo4j.Neo4jClient;
import io.vertx.up.plugin.neo4j.Neo4jInfix;

import java.util.function.Function;

abstract class AbstractPixel implements Pixel {

    protected final transient String identifier;
    protected final transient Neo4jClient client;

    public AbstractPixel(final String identifier) {
        this.identifier = identifier;
        this.client = Neo4jInfix.getClient();
        if (this.client.connected()) {
            this.client.connect(KWeb.DEPLOY.VERTX_GROUP);
        }
    }

    protected <T> Future<T> runSafe(final T input, final Function<T, Future<T>> executor) {
        return Ox.runSafe(this.getClass(), input, executor);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
