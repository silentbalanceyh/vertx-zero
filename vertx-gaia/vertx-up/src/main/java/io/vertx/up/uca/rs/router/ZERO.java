package io.vertx.up.uca.rs.router;

import io.vertx.ext.web.Route;
import io.vertx.up.uca.cache.Cc;

interface Info {

    String NULL_EVENT = "( {0} ) The system found \"null\" event in the queue. ";
}

interface Pool {
    Cc<String, Hub<Route>> CC_HUB_URI = Cc.openThread();
    Cc<String, Hub<Route>> CC_HUB_MEDIA = Cc.openThread();
}
