package io.vertx.up.extension.router;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Info {
    String DY_DETECT = "( {0} ) The system is detecting dynamic routing component...";

    String DY_SKIP = "( {0} ) Skip dynamic routing because clazz is null or class {1} is not assignable from \"io.vertx.up.extension.router.PlugRouter\".";

    String DY_FOUND = "( {0} ) Zero system detect class {1} ( io.vertx.up.extension.router.PlugRouter ) with config {2}.";
}
