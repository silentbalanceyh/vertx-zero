package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/*
 * Different quota for monitor here
 */
public interface Quota extends Handler<Promise<Status>> {

    ConcurrentMap<String, Function<Vertx, Quota>> REGISTRY_CLS = new ConcurrentHashMap<String, Function<Vertx, Quota>>() {
        {
            this.put("pool/habitus", HabitusQuota::new);
            this.put("instance", ThreadQuota::new);
        }
    };

    static ConcurrentMap<String, Quota> getRegistry(final Vertx vertx) {
        final ConcurrentMap<String, Quota> quotaMap = new ConcurrentHashMap<>();
        REGISTRY_CLS.forEach((uri, executor) -> quotaMap.put(uri, executor.apply(vertx)));
        return quotaMap;
    }
}
