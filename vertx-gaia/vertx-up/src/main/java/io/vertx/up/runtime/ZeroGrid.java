package io.vertx.up.runtime;

import io.vertx.core.ClusterOptions;
import io.vertx.core.ServidorOptions;
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
    private static final ConcurrentMap<Integer, ServidorOptions> RPC_OPTS =
            new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> RX_OPTS =
            new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, HttpServerOptions> SOCK_OPTS =
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
                final ServerVisitor<ServidorOptions> visitor =
                        Ut.singleton(RpcServerVisitor.class);
                RPC_OPTS.putAll(visitor.visit());
            }
            // Init for SockServerOptions
            if (SOCK_OPTS.isEmpty()) {
                final ServerVisitor<HttpServerOptions> visitor =
                        Ut.singleton(SockServerVisitor.class);
                SOCK_OPTS.putAll(visitor.visit());
            }
            // Init for all plugin options.
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

    public static ConcurrentMap<Integer, ServidorOptions> getRpcOptions() {
        return RPC_OPTS;
    }

    public static ConcurrentMap<Integer, HttpServerOptions> getSockOptions() {
        return SOCK_OPTS;
    }

    public static ClusterOptions getClusterOption() {
        return CLUSTER;
    }
}
