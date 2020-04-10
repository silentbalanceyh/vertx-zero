package io.vertx.up.uca.rs.router.monitor;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;

/*
 * Different quota for monitor here
 */
public interface Quota extends Handler<Promise<Status>> {

    static Quota pool(final Vertx vertx, final String name) {
        return new PoolQuota(vertx, name);
    }
}
