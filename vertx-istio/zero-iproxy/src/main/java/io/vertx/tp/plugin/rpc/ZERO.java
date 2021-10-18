package io.vertx.tp.plugin.rpc;

interface Info {

    String RPC_SERVICE = "( Rpc Client ) Lookup service: name = {0}, addr = {1}";

    String RPC_FOUND = "( Rpc Client ) Dynamic service record found: data = {0}";

    String CLIENT_BUILD = "Build channel for host = {0}, port = {1}";

    String CLIENT_RPC = "( Rpc Client ) Build channel ( host = {0}, port = {1}, hashCode = {2} )";

    String CLIENT_RESPONSE = "( Rpc Client ) Response Json data is {0}";

    String CLIENT_TRAFFIC = "( Rpc Client ) Final Traffic Data will be {0}";
}

interface Key {
    String RULE_KEY = "rpcclient";
    String PATH = "path";
    String ADDR = "addr";
    String NAME = "name";
    String TYPE = "type";

    String SSL = "ssl";
    String HOST = "host";
    String PORT = "port";
}