package io.vertx.up.boot.anima;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.log.DevOps;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Shared bottle method
 */
class Verticles {

    private static final ConcurrentMap<Class<?>, String> INSTANCES =
        new ConcurrentHashMap<>();

    static void deploy(final Vertx vertx,
                       final Class<?> clazz,
                       final DeploymentOptions option,
                       final Annal logger) {
        // Verticle deployment
        final String name = clazz.getName();
        final String flag = option.isWorker() ? "Worker" : "Agent";
        // Multi Thread worker enabled for trying.
        vertx.deployVerticle(name, option, (result) -> {
            // Success or Failed.
            if (result.succeeded()) {
                logger.info(VMessage.Verticle.END,
                    name, option.getInstances(), result.result(),
                    flag);
                INSTANCES.put(clazz, result.result());
                DevOps.on(vertx).add(name, option, result.result());
            } else {
                if (null != result.cause()) {
                    result.cause().printStackTrace();
                }
                logger.warn(VMessage.Verticle.FAILED,
                    name, option.getInstances(), result.result(),
                    null == result.cause() ? null : result.cause().getMessage(), flag);
            }
        });
    }

    static void undeploy(final Vertx vertx,
                         final Class<?> clazz,
                         final DeploymentOptions option,
                         final Annal logger) {
        // Verticle deployment
        final String name = clazz.getName();
        final String flag = option.isWorker() ? "Worker" : "Agent";
        final String id = INSTANCES.get(clazz);
        Fn.runAt(() -> vertx.undeploy(id, result -> {
            if (result.succeeded()) {
                logger.info(VMessage.Verticle.STOPPED, name, id, flag);
                DevOps.on(vertx).remove(clazz, option);
            }
        }), id);
    }
}
