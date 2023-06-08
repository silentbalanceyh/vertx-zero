package io.vertx.up.backbone.dispatch;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.backbone.hunt.IpcAim;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.ReturnTypeException;

import java.lang.reflect.Method;

class IpcDiffer implements Differ<RoutingContext> {

    private static final Annal LOGGER = Annal.get(IpcDiffer.class);

    private IpcDiffer() {
    }

    public static Differ<RoutingContext> create() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Aim<RoutingContext> build(final Event event) {
        final Method method = event.getAction();
        final Class<?> returnType = method.getReturnType();
        // Rpc Mode only
        Aim<RoutingContext> aim = null;
        if (Void.class == returnType || void.class == returnType) {
            // Exception because this method must has return type to
            // send message to event bus. It means that it require
            // return types.
            Fn.outBoot(true, LOGGER, ReturnTypeException.class,
                this.getClass(), method);
        } else {
            // Mode 6: Ipc channel enabled
            aim = CACHE.CC_AIMS.pick(() -> Ut.instance(IpcAim.class), "Mode Ipc");
            // Fn.po?l(Pool.AIMS, Thread.currentThread().getName() + "-mode-ipc",() -> Ut.instance(IpcAim.class));
        }
        return aim;
    }

    private static final class InstanceHolder {
        private static final Differ<RoutingContext> INSTANCE = new IpcDiffer();
    }
}
