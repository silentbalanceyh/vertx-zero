package io.vertx.up.verticle;

import io.vertx.core.ServidorOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.runtime.ZeroGrid;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public interface ZeroAtomic {
    /**
     * RPC Server information for gRPC part here, when you run zero framework
     * as micro environment, you can setup RPC Server in your environment and it's working
     * as Mash Mode here, in this kind of situation the gRPC server could do following
     * tasks such as:
     *
     * 1) Internal Long-Term working communication
     * 2) Internal communication to be instead of HTTP / RESTful
     * 3) Keep connection Alive always.
     */
    ConcurrentMap<Integer, ServidorOptions> RPC_OPTS = ZeroGrid.getRpcOptions();


    /**
     * HTTP Server information of standard in zero framework, it provide RESTful backend
     * application running on Vert.x instance here. Most situations, we configured this
     * part in real production environment.
     */
    ConcurrentMap<Integer, HttpServerOptions> HTTP_OPTS = ZeroGrid.getServerOptions();

    /*
     * Sock Server information, in current situation to avoid sharing information between
     * HTTP / SOCK, zero framework provide another way to mount SockJSHandler instead of
     * Sock Server, because the Sock Server could not share the session/store etc information
     * cross different HTTP servers, to avoid this design, I'll remove the socket server.
     *
     * The SockJSHandler will replace the socket server and it's working internal HTTP Server
     * instead.
     */
    // ConcurrentMap<Integer, HttpServerOptions> SOCK_OPTS = ZeroGrid.getSockOptions();

    /**
     * 「Reserved」
     * This configuration is for RxJava version of zero framework in future usage, it provide
     * another way to developers for reactive development instead of Callback/Future mode here.
     * You must involve another sub-project in zero framework to enable this structure
     */
    ConcurrentMap<Integer, HttpServerOptions> RX_OPTS = ZeroGrid.getRxOptions();


    /**
     * Api Gateway Server in micro service, this configuration is for micro-service api-gateway
     * only instead of other kind of Server instances.
     */
    ConcurrentMap<Integer, HttpServerOptions> API_OPTS = new ConcurrentHashMap<>();

    /**
     * One configuration for log output is enough here.
     */
    ConcurrentMap<Integer, AtomicInteger> ATOMIC_FLAG = new ConcurrentHashMap<>() {{
        // RPC Put
        RPC_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
        // HTTP Put
        HTTP_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
        // Rx Put
        RX_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
    }};
}
