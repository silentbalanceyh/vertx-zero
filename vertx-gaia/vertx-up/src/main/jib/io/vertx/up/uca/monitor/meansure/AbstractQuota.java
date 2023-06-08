package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractQuota implements Quota {
    protected final transient Vertx vertx;

    public AbstractQuota(final Vertx vertx) {
        this.vertx = vertx;
    }

    protected void mapAsync(final String name, final Consumer<AsyncMap<String, Object>> consumer) {
        this.vertx.sharedData().<String, Object>getAsyncMap(name, mapped -> {
            if (mapped.succeeded()) {
                consumer.accept(mapped.result());
            } else {
                if (Objects.nonNull(mapped.cause())) {
                    mapped.cause().printStackTrace();
                }
            }
        });
    }
}
