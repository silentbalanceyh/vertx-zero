package io.vertx.up.uca.rs.router.monitor;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.ext.healthchecks.Status;

public class PoolQuota implements Quota {
    private final transient Vertx vertx;
    private final transient String name;

    public PoolQuota(final Vertx vertx, final String name) {
        this.vertx = vertx;
        this.name = name;
    }

    @Override
    public void handle(final Promise<Status> event) {
        vertx.sharedData().<String, Object>getAsyncMap(name, mapped -> {
            if (mapped.succeeded()) {
                final AsyncMap<String, Object> map = mapped.result();
                /*
                 * map calculated
                 */
                final JsonObject meansure = new JsonObject();
                map.size(res -> {
                    meansure.put("size", res.result());
                    map.keys(keySet -> {
                        /*
                         * keys
                         */
                        final JsonArray keys = new JsonArray();
                        keySet.result().forEach(keys::add);
                        meansure.put("keys", keys);
                        /*
                         * Meansure here
                         */
                        event.complete(Status.OK(meansure));
                    });
                });
            } else {
                mapped.cause().printStackTrace();
            }
        });
    }
}
