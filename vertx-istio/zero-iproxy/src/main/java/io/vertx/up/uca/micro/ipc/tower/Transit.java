package io.vertx.up.uca.micro.ipc.tower;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.commune.Envelop;

import java.lang.reflect.Method;

/**
 * Different workflow for method call
 */
public interface Transit {
    /**
     * Async mode
     *
     * @return
     */
    Future<Envelop> async(Envelop data);

    /**
     * @param method
     *
     * @return
     */
    @Fluent
    Transit connect(Method method);

    @Fluent
    Transit connect(Vertx vertx);
}
