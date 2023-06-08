package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.ext.healthchecks.Status;
import io.vertx.up.eon.KWeb;
import io.vertx.up.fn.Fn;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class VerticleQuota extends AbstractQuota {
    VerticleQuota(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final Promise<Status> event) {
        // Context reference
        this.mapAsync(KWeb.SHARED.DEPLOYMENT, map -> {
            /*
             * map calculated
             */
            final ConcurrentMap<String, Future<JsonObject>> mapped =
                new ConcurrentHashMap<>();
            map.keys(keyRes -> {
                if (keyRes.succeeded()) {
                    final Set<String> keys = keyRes.result();
                    keys.forEach(key -> mapped.put(key, this.readAsync(key, map)));
                }
            });
            final Future<ConcurrentMap<String, JsonObject>> future = Fn.combineM(mapped);
            future.onComplete(handler -> {
                if (handler.succeeded()) {
                    final JsonObject meansure = new JsonObject();
                    final ConcurrentMap<String, JsonObject> result = handler.result();
                    result.forEach(meansure::put);
                    event.complete(Status.OK(meansure));
                }
            });
        });
    }

    private Future<JsonObject> readAsync(final String name, final AsyncMap<String, Object> map) {
        final Promise<JsonObject> promise = Promise.promise();
        map.get(name, res -> {
            if (res.succeeded()) {
                final JsonObject completed = (JsonObject) res.result();
                promise.complete(completed);
            }
        });
        return promise.future();
    }
}
