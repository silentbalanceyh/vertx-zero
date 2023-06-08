package io.vertx.rx.web.anima;

import io.horizon.eon.VMessage;
import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava3.core.Vertx;

class Verticles {

    static void deploy(final Vertx vertx,
                       final Class<?> clazz,
                       final DeploymentOptions options,
                       final Annal logger) {
        final String name = clazz.getName();
        final String flag = options.isWorker() ? "Rx-Worker" : "Rx-Agent";
        final Single<String> observable
            = vertx.rxDeployVerticle(clazz.getName(), options);
        observable.subscribe(
                (item) -> logger.info(VMessage.Verticle.END, name, options.getInstances(), item, flag),
                (cause) -> logger.info(VMessage.Verticle.FAILED, name, options.getInstances(),
                    null == cause.getCause() ? null : cause.getCause().getMessage(), flag))
            .dispose();
    }
}
