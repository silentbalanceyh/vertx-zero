package io.vertx.up.plugin.redis;

interface RedisMsg {

    String RD_CLOSE = "( Redis ) The client has been closed successfully !";
    String RD_CLEAR = "( Redis ) The client has been removed: {0}";
    String RD_KEYS = "( Redis ) Set key: {0}";
    String RD_OPTS = "( Redis ) Connect to Endpoint ( {0} ) Options read: {1}";
    String RS_MESSAGE = "( Redis ) Call {1} method: id = {0}";
    String RS_AFTER = "( Redis ) Session New = {0} / Old = {1}";
}
