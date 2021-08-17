package io.vertx.up.uca.rs.monitor.meansure;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.Status;
import io.vertx.up.eon.Constants;

class HabitusQuota extends AbstractQuota {

    HabitusQuota(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final Promise<Status> event) {
        this.mapAsync(Constants.Pool.HABITUS, map -> {
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
        });
    }
}
