package io.vertx.up.log;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.KName;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Meansure {
    private Meansure() {
    }

    static void add(final Vertx vertx, final String name, final DeploymentOptions options, final String id) {
        vertx.sharedData().<String, Object>getAsyncMap(Constants.Pool.DEPLOYMENT, mapped -> {
            if (mapped.succeeded()) {
                final JsonObject instance = new JsonObject();
                instance.put(KName.DEPLOY_ID, id);
                instance.put(KName.TYPE, name);
                instance.put("isWorker", options.isWorker());
                instance.put("instances", options.getInstances());
                instance.put("poolName", options.getWorkerPoolName());
                instance.put("poolSize", options.getWorkerPoolSize());
                final AsyncMap<String, Object> data = mapped.result();
                data.put(name, instance, result -> {
                    if (result.succeeded()) {
                        final Annal logger = Annal.get(Meansure.class);
                        logger.info(Info.MEANSURE_ADD, name,
                                String.valueOf(options.getInstances()), options.isWorker());
                    }
                });
            }
        });
    }

    static void remove(final Vertx vertx, final String name, final DeploymentOptions options) {
        vertx.sharedData().<String, Object>getAsyncMap(Constants.Pool.DEPLOYMENT, mapped -> {
            if (mapped.succeeded()) {
                final AsyncMap<String, Object> data = mapped.result();
                data.remove(name, result -> {
                    if (result.succeeded()) {
                        final Annal logger = Annal.get(Meansure.class);
                        logger.info(Info.MEANSURE_REMOVE, name, String.valueOf(options.getInstances()));
                    }
                });
            }
        });
    }
}
