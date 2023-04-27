package io.vertx.up.uca.rs.dispatch;

import io.vertx.ext.web.RoutingContext;
import io.horizon.uca.cache.Cc;
import io.vertx.up.uca.rs.Aim;

interface Pool {
    Cc<String, Aim<RoutingContext>> CC_AIMS = Cc.openThread();
}
