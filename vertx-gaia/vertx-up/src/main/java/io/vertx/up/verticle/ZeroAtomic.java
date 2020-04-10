package io.vertx.up.verticle;

import io.vertx.core.ServidorOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.runtime.ZeroGrid;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public interface ZeroAtomic {
    /**
     * Rpc constant
     */
    ConcurrentMap<Integer, ServidorOptions> RPC_OPTS =
            ZeroGrid.getRpcOptions();
    ConcurrentMap<Integer, AtomicInteger>
            RPC_START_LOGS = new ConcurrentHashMap<Integer, AtomicInteger>() {
        {
            RPC_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
        }
    };
    /**
     * Http constants
     */
    ConcurrentMap<Integer, HttpServerOptions>
            HTTP_OPTS = ZeroGrid.getServerOptions();
    ConcurrentMap<Integer, AtomicInteger>
            HTTP_START_LOGS = new ConcurrentHashMap<Integer, AtomicInteger>() {
        {
            HTTP_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
        }
    };
    /**
     * Rx constants
     */
    ConcurrentMap<Integer, HttpServerOptions>
            RX_OPTS = ZeroGrid.getRxOptions();
    ConcurrentMap<Integer, AtomicInteger>
            RX_START_LOGS = new ConcurrentHashMap<Integer, AtomicInteger>() {
        {
            RX_OPTS.forEach((port, option) -> this.put(port, new AtomicInteger(0)));
        }
    };
    /**
     * Socket constants
     */
    ConcurrentMap<Integer, HttpServerOptions>
            SOCK_OPTS = ZeroGrid.getSockOptions();
    /**
     * Api Gateay
     */
    ConcurrentMap<Integer, HttpServerOptions> API_OPTS =
            new ConcurrentHashMap<>();
}
