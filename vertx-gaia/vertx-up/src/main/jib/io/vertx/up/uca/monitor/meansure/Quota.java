package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Different quota for monitor here
 */
public interface Quota extends Handler<Promise<Status>> {

    static ConcurrentMap<String, Quota> getRegistry(final Vertx vertx) {
        final ConcurrentMap<String, Quota> quotaMap = new ConcurrentHashMap<>();
        QuotaConnect.REGISTRY_CLS.forEach((uri, executor) -> quotaMap.put(uri, executor.apply(vertx)));
        return quotaMap;
    }
}
