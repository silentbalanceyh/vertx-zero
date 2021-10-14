package cn.originx.uca.elasticsearch;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.up.log.Annal;

import java.util.function.Supplier;

abstract class AbstractEsIndex implements EsIndex {

    protected final transient String identifier;
    protected final transient ElasticSearchClient client;

    public AbstractEsIndex(final String identifier) {
        this.identifier = identifier;
        this.client = ElasticSearchInfix.getClient();
    }

    protected <T> Future<T> runSingle(final T input, final Supplier<T> supplier) {
        return Ox.runSafe(this.getClass(), input, supplier);
    }

    protected Future<JsonArray> runBatch(final JsonArray input, final Supplier<Boolean> supplier) {
        return Ox.runSafe(this.getClass(), input, () -> {
            final Boolean batched = supplier.get();
            this.logger().info("EsIndex result = {0}", batched);
            return input;
        });
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
