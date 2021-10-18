package io.vertx.up.uca.micro.ipc.server;

import io.grpc.BindableService;
import io.vertx.core.Vertx;
import io.vertx.up.runtime.ZeroAnno;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * Rpc Service
 */
public interface Tunnel {
    /**
     * IPC method annotated with @Ipc
     */
    ConcurrentMap<String, Method> IPCS
        = ZeroAnno.getIpcs();

    /**
     * Create new Rpc Service by type
     *
     * @return BindableService that will be used in IPC
     */
    BindableService init(Vertx vertx);
}
