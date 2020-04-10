package io.vertx.up.uca.web.anima;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.eon.Info;
import io.vertx.up.log.Annal;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Shared verticle method
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
        if (option.isWorker()) {
            option.setMultiThreaded(true);
        }
        vertx.deployVerticle(name, option, (result) -> {
            // Success or Failed.
            if (result.succeeded()) {
                logger.info(Info.VTC_END,
                        name, option.getInstances(), result.result(),
                        flag);
                INSTANCES.put(clazz, result.result());
            } else {
                if (null != result.cause()) {
                    result.cause().printStackTrace();
                }
                logger.warn(Info.VTC_FAIL,
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
        Fn.safeNull(() -> vertx.undeploy(id, result -> {
            if (result.succeeded()) {
                logger.info(Info.VTC_STOPPED, name, id, flag);
            }
        }), id);
    }
}
