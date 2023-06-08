package io.vertx.up.backbone.dispatch;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.backbone.Aim;

interface CACHE {
    @Memory(Aim.class)
    Cc<String, Aim<RoutingContext>> CC_AIMS = Cc.openThread();
}
