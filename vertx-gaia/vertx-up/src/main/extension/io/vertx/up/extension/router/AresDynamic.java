package io.vertx.up.extension.router;

import io.horizon.eon.VValue;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 「Extension」
 * Name: Dynamic Router
 * Basic dynamic spec extension, every dynamic router must implements this interface for building dynamic routing system.
 * 1. The router system will mount to default Order: 6_000_000, it means that
 * dynamic router priority is lower than ZERO Standard: ( 5_000_000 ).
 * 2. There is a default method implementation and to nothing, one you have no implementation in
 * extension plugins, it's also available and do not impact Standard Part.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AresDynamic extends AbstractAres {

    private static final AtomicInteger LOG_FLAG_START = new AtomicInteger(0);
    private static final AtomicInteger LOG_FLAG_END = new AtomicInteger(0);

    private final JsonObject configuration = new JsonObject();

    AresDynamic(final Vertx vertxRef) {
        super(vertxRef);
        final JsonObject routerJ = ZeroStore.option(YmlCore.router.__KEY);
        this.configuration.mergeIn(routerJ, true);
    }

    /*
     * Dynamic Extension for some user-defined router to resolve some spec
     * requirement such as Data Driven System and Origin X etc.
     * Call second method to inject vertx reference.
     */
    @Override
    public void mount(final Router router, final JsonObject config) {
        final String name = this.getClass().getSimpleName();
        // Extract class that are implemented
        final Class<?> clazz = ZeroStore.injection(YmlCore.router.__KEY); // ZeroAmbient.getPlugin(KName.ROUTER);
        if (VValue.ZERO == LOG_FLAG_START.getAndIncrement()) {
            this.logger().info(INFO.DYNAMIC_DETECT, name);
        }
        // The condition for skip
        if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Ares.class)) {
            final JsonObject configRouter = this.configuration.copy();
            if (VValue.ONE == LOG_FLAG_END.getAndIncrement()) {
                this.logger().info(INFO.DYNAMIC_FOUND, name, clazz.getName(), configRouter.encode());
            }

            // Mount Ares of Dynamic Router
            final Ares dynamic = Ares.CC_ARES.pick(() -> Ut.instance(clazz, this.vertx()),
                clazz.getName() + this.vertx().hashCode());
            dynamic.bind(this.server, this.options);
            dynamic.mount(router, configRouter);
        } else {
            if (VValue.ONE == LOG_FLAG_END.getAndIncrement()) {
                final String className = Fn.runOr(null, () -> null == clazz ? null : clazz.getName(), clazz);
                this.logger().info(INFO.DYNAMIC_SKIP, name, className);
            }
        }
    }
}
