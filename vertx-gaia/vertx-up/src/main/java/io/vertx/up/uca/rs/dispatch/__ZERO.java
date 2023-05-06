package io.vertx.up.uca.rs.dispatch;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.Aim;

interface CACHE {
    @Memory(Aim.class)
    Cc<String, Aim<RoutingContext>> CC_AIMS = Cc.openThread();
}
