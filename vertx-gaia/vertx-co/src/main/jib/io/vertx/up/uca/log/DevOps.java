package io.vertx.up.uca.log;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;

/**
 * @author lang : 2023/4/25
 */
public class DevOps {
    private final transient Vertx vertx;

    private DevOps(final Vertx vertx) {
        this.vertx = vertx;
    }

    public static DevOps on(final Vertx vertx) {
        return new DevOps(vertx);
    }


    private static void add(final Vertx vertx, final String name, final DeploymentOptions options, final String id) {
        vertx.sharedData().<String, Object>getAsyncMap(KWeb.SHARED.DEPLOYMENT, mapped -> {
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
                        final Annal logger = Annal.get(DevOps.class);
                        logger.info(VMessage.Measure.ADD, name,
                            String.valueOf(options.getInstances()), options.isWorker());
                    }
                });
            }
        });
    }

    private static void remove(final Vertx vertx, final String name, final DeploymentOptions options) {
        vertx.sharedData().<String, Object>getAsyncMap(KWeb.SHARED.DEPLOYMENT, mapped -> {
            if (mapped.succeeded()) {
                final AsyncMap<String, Object> data = mapped.result();
                data.remove(name, result -> {
                    if (result.succeeded()) {
                        final Annal logger = Annal.get(DevOps.class);
                        logger.info(VMessage.Measure.REMOVE, name, String.valueOf(options.getInstances()));
                    }
                });
            }
        });
    }

    public void add(final Class<?> clazz, final DeploymentOptions options, final String id) {
        add(this.vertx, clazz.getName(), options, id);
    }

    public void add(final String name, final DeploymentOptions options, final String id) {
        add(this.vertx, name, options, id);
    }

    public void remove(final Class<?> clazz, final DeploymentOptions options) {
        remove(this.vertx, clazz.getName(), options);
    }
}
