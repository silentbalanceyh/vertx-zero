package io.vertx.up.backbone.router;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.vertx.ext.web.Route;

interface INFO {

    String NULL_EVENT = "( {0} ) The system found \"null\" event in the queue. ";
}

interface CACHE {
    @Memory(Hub.class)
    Cc<String, Hub<Route>> CC_HUB_URI = Cc.openThread();
    @Memory(Hub.class)
    Cc<String, Hub<Route>> CC_HUB_MEDIA = Cc.openThread();
}
