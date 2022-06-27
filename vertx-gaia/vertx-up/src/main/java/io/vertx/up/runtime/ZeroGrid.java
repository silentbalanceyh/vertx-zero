package io.vertx.up.runtime;

import io.vertx.core.ClusterOptions;
import io.vertx.core.RpcOptions;
import io.vertx.core.SockOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.options.*;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Resource ZeroPack for yml configuration, Loaded once
 */
public class ZeroGrid {

    private static final Annal LOGGER = Annal.get(ZeroGrid.class);
    private static final ConcurrentMap<String, VertxOptions> VX_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> SERVER_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, String> SERVER_NAMES =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, RpcOptions> RPC_OPTS =
        new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> RX_OPTS =
        new ConcurrentHashMap<>();

    private static final ConcurrentMap<Integer, SockOptions> SOCK_OPTS =
        new ConcurrentHashMap<>();
    private static ClusterOptions CLUSTER;

    static {
        Fn.outUp(() -> {
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
            ZeroAmbient.init();
        }, LOGGER);
    }

    public static ConcurrentMap<String, VertxOptions> getVertxOptions() {
        return VX_OPTS;
    }

    public static ConcurrentMap<Integer, HttpServerOptions> getServerOptions() {
        return SERVER_OPTS;
    }

    public static ConcurrentMap<Integer, String> getServerNames() {
        return SERVER_NAMES;
    }

    public static ConcurrentMap<Integer, HttpServerOptions> getRxOptions() {
        return RX_OPTS;
    }

    public static ConcurrentMap<Integer, RpcOptions> getRpcOptions() {
        return RPC_OPTS;
    }

    public static ConcurrentMap<Integer, SockOptions> getSockOptions() {
        return SOCK_OPTS;
    }

    public static ClusterOptions getClusterOption() {
        return CLUSTER;
    }
}
