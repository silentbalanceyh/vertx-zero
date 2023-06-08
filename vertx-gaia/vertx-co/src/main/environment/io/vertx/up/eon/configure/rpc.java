package io.vertx.up.eon.configure;

/**
 * @author lang : 2023-05-29
 */
interface YmlRpc {
    String __KEY = "rpc";
    String EXTENSION = "extension";
    String UNIFORM = "uniform";
    String SSL = "ssl";

    String TYPE = "type";

    // Client 专用
    interface client {
        String RPC_CLIENT = "rpc_client";
        String ADDR = "addr";
        String NAME = "name";
        String PATH = "path";
        String HOST = "host";
        String PORT = "port";
    }
}
