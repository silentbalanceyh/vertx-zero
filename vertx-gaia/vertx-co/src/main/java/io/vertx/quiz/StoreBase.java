package io.vertx.quiz;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

public abstract class StoreBase extends ZeroBase {

    protected void execJObject(final String filename,
                               final Handler<AsyncResult<JsonObject>> handler) {
        final ConfigStoreOptions options = Ut.loadJson(this.ioFile(filename));
        this.exec(options, handler);
    }

    protected void execYaml(final String filename,
                            final Handler<AsyncResult<JsonObject>> handler) {
        final ConfigStoreOptions options = Ut.loadYaml(this.ioFile(filename));
        this.exec(options, handler);
    }

    protected void execProp(final String filename,
                            final Handler<AsyncResult<JsonObject>> handler) {
        final ConfigStoreOptions options = Ut.loadProp(this.ioFile(filename));
        this.exec(options, handler);
    }

    private void exec(final ConfigStoreOptions options,
                      final Handler<AsyncResult<JsonObject>> handler) {
        final ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions().addStore(options);
        final Vertx vertx = this.rule.vertx();
        final ConfigRetriever retriever = ConfigRetriever.create(vertx, retrieverOptions);
        retriever.getConfig(handler);
    }
}
