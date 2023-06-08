package io.vertx.up.backbone.dispatch;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.fn.Fn;
import io.vertx.zero.exception.ChannelMultiException;

import java.lang.reflect.Method;

/**
 * Splitter to getNull executor reference.
 * It will happen in startup of route building to avoid
 * request resource spending.
 * 1. Level 1: Distinguish whether enable EventBus
 * EventBus mode: Async
 * Non-EventBus mode: Sync
 * 2. Level 2: Distinguish the request mode
 * One-Way mode: No response needed. ( Return Type )
 * Request-Response mode: Must require response. ( Return Type )
 * Support modes:
 * 1. AsyncAim: Event Bus: Request-Response
 * 2. SyncAim: Non-Event Bus: Request-Response
 * 3. OneWayAim: Event Bus: One-Way
 * 4. BlockAim: Non-Event Bus: One-Way
 * 5. Vert.x Style Request -> Event -> Response
 * 6. Rpc Style for @Ipc annotation
 */
public class ModeSplitter {

    private static final Annal LOGGER = Annal.get(ModeSplitter.class);

    public Aim<RoutingContext> distribute(final Event event) {
        return Fn.runOr(() -> {
            // 1. Scan method to check @Address
            final Method method = event.getAction();
            final boolean annotated = method.isAnnotationPresent(Address.class);
            final boolean rpc = method.isAnnotationPresent(Ipc.class);
            // 2. Only one channel enabled
            Fn.outBoot(rpc && annotated, LOGGER, ChannelMultiException.class,
                this.getClass(), method);

            final Differ<RoutingContext> differ;
            if (annotated) {
                // EventBus Mode for Mode: 1,3,5
                differ = EventDiffer.create();
            } else if (rpc) {

                // Ipc Mode for Mode: 6
                differ = IpcDiffer.create();
            } else {

                // Non Event Bus for Mode: 2,4
                differ = CommonDiffer.create();
            }
            return differ.build(event);
        }, event, event.getAction());
    }
}
