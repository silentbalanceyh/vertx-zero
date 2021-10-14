package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import io.vertx.up.uca.cache.RapidKey;

class SessionQuota extends AbstractQuota {

    SessionQuota(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final Promise<Status> event) {
        this.mapAsync(RapidKey.User.MY_HABITUS, map -> {
            /*
             * map calculated
             */
            final JsonObject sessions = new JsonObject();
            map.size(res -> {
                sessions.put("size", res.result());
                map.keys(keySet -> {
                    /*
                     * keys
                     */
                    final JsonArray keys = new JsonArray();
                    keySet.result().forEach(keys::add);
                    sessions.put("keys", keys);
                    /*
                     * Meansure here
                     */
                    event.complete(Status.OK(sessions));
                });
            });
        });
    }
}
