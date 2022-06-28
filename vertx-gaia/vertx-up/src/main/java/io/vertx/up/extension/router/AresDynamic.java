package io.vertx.up.extension.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.Ares;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
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

    private static final Node<JsonObject> UNIFORM = Ut.singleton(ZeroUniform.class);
    private static final AtomicInteger LOG_FLAG_START = new AtomicInteger(0);
    private static final AtomicInteger LOG_FLAG_END = new AtomicInteger(0);

    private final JsonObject configuration = new JsonObject();

    AresDynamic(final Vertx vertxRef) {
        super(vertxRef);
        final JsonObject config = UNIFORM.read();
        this.configuration.mergeIn(Ut.valueJObject(config, KName.ROUTER), true);
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
        final Class<?> clazz = ZeroAmbient.getPlugin(KName.ROUTER);
        if (Values.ZERO == LOG_FLAG_START.getAndIncrement()) {
            this.logger().info(Info.DYNAMIC_DETECT, name);
        }
        // The condition for skip
        if (Objects.nonNull(clazz) && Ut.isImplement(clazz, Ares.class)) {
            final JsonObject configRouter = this.configuration.copy();
            if (Values.ONE == LOG_FLAG_END.getAndIncrement()) {
                this.logger().info(Info.DYNAMIC_FOUND, name, clazz.getName(), configRouter.encode());
            }

            // Mount Ares of Dynamic Router
            final Ares dynamic = Ares.CC_ARES.pick(() -> Ut.instance(clazz, this.vertx()),
                clazz.getName() + this.vertx().hashCode());
            dynamic.bind(this.server, this.options);
            dynamic.mount(router, configRouter);
        } else {
            if (Values.ONE == LOG_FLAG_END.getAndIncrement()) {
                final String className = Fn.getNull(null, () -> null == clazz ? null : clazz.getName(), clazz);
                this.logger().info(Info.DYNAMIC_SKIP, name, className);
            }
        }
    }
}
