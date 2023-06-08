package io.vertx.up.runtime;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.ClusterOptions;
import io.vertx.core.RpcOptions;
import io.vertx.core.SockOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.boot.deployment.DeployRotate;
import io.vertx.up.boot.deployment.Rotate;
import io.vertx.up.boot.options.*;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.options.NodeVisitor;
import io.vertx.up.uca.options.RpcServerVisitor;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Resource ZeroPack for yml configuration, Loaded once
 */
public class ZeroOption {

    private static final Annal LOGGER = Annal.get(ZeroOption.class);
    private static final ConcurrentMap<String, VertxOptions> VX_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> SERVER_OPTS =
        new ConcurrentHashMap<>();

    private static final ConcurrentMap<Integer, HttpServerOptions> GATEWAY_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, String> SERVER_NAMES =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, RpcOptions> RPC_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> RX_OPTS =
        new ConcurrentHashMap<>();

    private static final ConcurrentMap<Integer, SockOptions> SOCK_OPTS =
        new ConcurrentHashMap<>();
    private static final Cc<String, Rotate> CC_ROTATE = Cc.openThread();
    public static ConcurrentMap<Integer, AtomicInteger> ATOMIC_LOG = new ConcurrentHashMap<>();
    private static ClusterOptions CLUSTER;

    static {

        Fn.outBug(() -> {
            // Init for VertxOptions, ClusterOptions
            // Visit Vertx
            if (VX_OPTS.isEmpty() || null == CLUSTER) {
                final NodeVisitor visitor =
                    Ut.singleton(VertxVisitor.class);
                VX_OPTS.putAll(visitor.visit());
                // Must after visit
                CLUSTER = visitor.getCluster();
            }
            // Init for HttpServerOptions
            if (SERVER_OPTS.isEmpty()) {
                final ServerVisitor<HttpServerOptions> visitor =
                    Ut.singleton(HttpServerVisitor.class);
                SERVER_OPTS.putAll(visitor.visit());
                // Secondary
                if (SERVER_NAMES.isEmpty()) {
                    final ServerVisitor<String> VISITOR =
                        Ut.singleton(NamesVisitor.class);
                    SERVER_NAMES.putAll(VISITOR.visit(ServerType.HTTP.toString()));
                }
            }
            // Init for GatewayOptions
            if (GATEWAY_OPTS.isEmpty()) {
                final ServerVisitor<HttpServerOptions> visitor =
                    Ut.singleton(DynamicVisitor.class);
                GATEWAY_OPTS.putAll(visitor.visit(ServerType.API.toString()));
            }
            // Init for RxServerOptions
            if (RX_OPTS.isEmpty()) {
                final ServerVisitor<HttpServerOptions> visitor =
                    Ut.singleton(RxServerVisitor.class);
                RX_OPTS.putAll(visitor.visit());
            }
            // Init for RpxServerOptions
            if (RPC_OPTS.isEmpty()) {
                final ServerVisitor<RpcOptions> visitor =
                    Ut.singleton(RpcServerVisitor.class);
                RPC_OPTS.putAll(visitor.visit());
            }
            if (SOCK_OPTS.isEmpty()) {
                final ServerVisitor<SockOptions> visitor =
                    Ut.singleton(SockVisitor.class);
                final ConcurrentMap<Integer, SockOptions> map = visitor.visit();
                map.keySet().stream().filter(SERVER_OPTS::containsKey)
                    .forEach(port -> SOCK_OPTS.put(port, map.get(port)));
            }
            // Init for ATOMIC_LOG
            {
                // RPC Put
                RPC_OPTS.forEach((port, option) -> ATOMIC_LOG.put(port, new AtomicInteger(0)));
                // HTTP Put
                SERVER_OPTS.forEach((port, option) -> ATOMIC_LOG.put(port, new AtomicInteger(0)));
                // Rx Put
                RX_OPTS.forEach((port, option) -> ATOMIC_LOG.put(port, new AtomicInteger(0)));
                // Api Put
                GATEWAY_OPTS.forEach((port, option) -> ATOMIC_LOG.put(port, new AtomicInteger(0)));
            }
            // ZeroAmbient.init();
        }, LOGGER);
    }

    public static ConcurrentMap<String, VertxOptions> getVertxOptions() {
        return VX_OPTS;
    }

    /**
     * HTTP Server information of standard in zero framework, it provide RESTful backend
     * application running on Vert.x instance here. Most situations, we configured this
     * part in real production environment.
     */
    public static ConcurrentMap<Integer, HttpServerOptions> getServerOptions() {
        return SERVER_OPTS;
    }

    /**
     * Api Gateway Server in micro service, this configuration is for micro-service api-gateway
     * only instead of other kind of Server instances.
     */
    public static ConcurrentMap<Integer, HttpServerOptions> getGatewayOptions() {
        return GATEWAY_OPTS;
    }

    /*
     * Sock Server information, in current situation to avoid sharing information between
     * HTTP / SOCK, zero framework provide another way to mount SockJSHandler instead of
     * Sock Server, because the Sock Server could not share the session/store etc information
     * cross different HTTP servers, to avoid this design, I'll remove the socket server.
     *
     * The SockJSHandler will replace the socket server and it's working internal HTTP Server
     * instead.
     */
    public static ConcurrentMap<Integer, SockOptions> getSockOptions() {
        return SOCK_OPTS;
    }

    public static ConcurrentMap<Integer, String> getServerNames() {
        return SERVER_NAMES;
    }

    /**
     * 「Reserved」
     * This configuration is for RxJava version of zero framework in future usage, it provide
     * another way to developers for reactive development instead of Callback/Future mode here.
     * You must involve another sub-project in zero framework to enable this structure
     */
    public static ConcurrentMap<Integer, HttpServerOptions> getRxOptions() {
        return RX_OPTS;
    }

    /**
     * RPC Server information for gRPC part here, when you run zero framework
     * as micro environment, you can setup RPC Server in your environment and it's working
     * as Mash Mode here, in this kind of situation the gRPC server could do following
     * tasks such as:
     * 1) Internal Long-Term working communication
     * 2) Internal communication to be instead of HTTP / RESTful
     * 3) Keep connection Alive always.
     */
    public static ConcurrentMap<Integer, RpcOptions> getRpcOptions() {
        return RPC_OPTS;
    }

    public static ClusterOptions getClusterOption() {
        return CLUSTER;
    }

    /*
     * Unity configuration management
     * In future, all the configuration management will be here for
     * uniform calling,
     * Default for Event Bus
     */
    public static DeliveryOptions getDeliveryOption() {
        final Rotate rotate = CC_ROTATE.pick(DeployRotate::new); // Fn.po?lThread(this.ROTATE, DeployRotate::new);
        return rotate.spinDelivery();
    }
}
