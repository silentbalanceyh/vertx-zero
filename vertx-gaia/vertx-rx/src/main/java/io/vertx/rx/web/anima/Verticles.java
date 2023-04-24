package io.vertx.rx.web.anima;

import io.horizon.eon.info.VMessage;
import io.reactivex.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.up.log.Annal;

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
                (item) -> logger.info(VMessage.VERTX_END, name, options.getInstances(), item, flag),
                (cause) -> logger.info(VMessage.VERTX_FAIL, name, options.getInstances(),
                    null == cause.getCause() ? null : cause.getCause().getMessage(), flag))
            .dispose();
    }
}
