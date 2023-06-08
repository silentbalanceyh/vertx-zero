package io.vertx.up.extension.router;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface INFO {
    String DYNAMIC_DETECT = "( {0} ) The system is detecting dynamic routing component...";

    String DYNAMIC_SKIP = "( {0} ) Skip dynamic routing because clazz is null or class {1} is not assignable from \"io.vertx.up.extension.router.PlugRouter\".";

    String DYNAMIC_FOUND = "( {0} ) Zero system detect class {1} ( io.vertx.up.extension.router.PlugRouter ) with config {2}.";

    String WS_DISABLED = "[ Sock ] The WebSocket has been disabled on server: {1}:{0}";

    String WS_PUBLISH = "[ Sock ] The WebSocket publish channel has been opened: http://{1}:{0}{2}";

    String WS_COMPONENT = "[ Sock ] The WebSocket component has been selected: {0}";
}
