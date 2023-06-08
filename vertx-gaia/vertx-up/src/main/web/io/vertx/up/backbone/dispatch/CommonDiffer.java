package io.vertx.up.backbone.dispatch;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.backbone.hunt.PingAim;
import io.vertx.up.backbone.hunt.SyncAim;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;

/**
 * EventBus disabled for request
 * 1. SyncAim: Non-Event Bus: Request -> Response
 * 2. BlockAim: Non-Event Bus: Request -> (TRUE/FALSE)
 */
class CommonDiffer implements Differ<RoutingContext> {

    private CommonDiffer() {
    }

    public static Differ<RoutingContext> create() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Aim<RoutingContext> build(final Event event) {
        final Method method = event.getAction();
        final Class<?> returnType = method.getReturnType();
        Aim<RoutingContext> aim = null;
        if (Void.class == returnType || void.class == returnType) {
            // Mode 4: Non-Event Bus: One-Way
            aim = CACHE.CC_AIMS.pick(() -> Ut.instance(PingAim.class), "Mode Ping");
            // Fn.po?l(Pool.AIMS, Thread.currentThread().getName() + "-mode-ping", () -> Ut.instance(PingAim.class));
        } else {
            // Mode 2: Non-Event Bus: Request-Response\
            aim = CACHE.CC_AIMS.pick(() -> Ut.instance(SyncAim.class), "Mode Sync");
            // Fn.po?l(Pool.AIMS, Thread.currentThread().getName() + "-mode-sync", () -> Ut.instance(SyncAim.class));
        }
        return aim;
    }

    private static final class InstanceHolder {
        private static final Differ<RoutingContext> INSTANCE = new CommonDiffer();
    }
}
