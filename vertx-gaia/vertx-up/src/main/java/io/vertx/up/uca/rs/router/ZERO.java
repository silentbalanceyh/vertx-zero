package io.vertx.up.uca.rs.router;

import io.vertx.ext.web.Route;
import io.vertx.up.uca.cache.Cc;

interface Info {

    String NULL_EVENT = "( {0} ) The system found \"null\" event in the queue. ";

    String DY_DETECT = "( {0} ) The system is detecting dynamic routing component...";

    String DY_SKIP = "( {0} ) Skip dynamic routing because clazz is null or class {1} is not assignable from \"io.vertx.up.extension.PlugRouter\".";

    String DY_FOUND = "( {0} ) Zero system detect class {1} ( io.vertx.up.extension.PlugRouter ) with config {2}.";
}

interface Pool {
    Cc<String, Hub<Route>> CC_HUB_URI = Cc.openThread();
    Cc<String, Hub<Route>> CC_HUB_MEDIA = Cc.openThread();
}
