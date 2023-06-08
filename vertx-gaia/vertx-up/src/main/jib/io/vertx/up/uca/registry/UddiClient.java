package io.vertx.up.uca.registry;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.commune.Envelop;

import java.lang.reflect.Method;

/*
 * Uddi Client
 */
public interface UddiClient {

    UddiClient bind(Vertx vertx);

    UddiClient bind(Method method);

    Future<Envelop> connect(final Envelop envelop);
}
