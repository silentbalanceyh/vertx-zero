package io.vertx.up.eon.configure;

/**
 * @author lang : 2023-05-29
 */
interface YmlEtcd {
    String __KEY = "etcd";
    String MICRO = "micro";
    String NODES = "nodes";
    String TIMEOUT = "timeout";

    interface nodes {
        String HOST = "host";
        String PORT = "port";
    }
}
