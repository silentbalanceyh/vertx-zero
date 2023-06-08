package io.vertx.up.backbone.invoker;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.modello.eon.em.EmValue;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Me;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.registry.Uddi;
import io.vertx.up.uca.registry.UddiClient;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Uniform call TunnelClient to remove duplicated codes
 * Refactor invokder to support Dynamic Invoke
 */
@SuppressWarnings("all")
public abstract class AbstractInvoker implements Invoker {

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    /**
     * Future method(JsonObject)
     * Future method(JsonArray)
     */
    protected Future invokeJson(
        final Object proxy,
        final Method method,
        final Envelop envelop) {
        // Preparing Method
        invokePre(method, envelop);
        final Object reference = envelop.data();
        final Class<?> argType = method.getParameterTypes()[VValue.IDX];
        final Object arguments = Ut.deserialize(Ut.toString(reference), argType);
        return InvokerUtil.invoke(proxy, method, arguments);
    }

    private void invokePre(final Method method, final Envelop envelop) {
        if (method.isAnnotationPresent(Me.class)) {
            final Annotation annotation = method.getDeclaredAnnotation(Me.class);
            final EmValue.Bool active = Ut.invoke(annotation, "active");
            final boolean app = Ut.invoke(annotation, "app");
            envelop.onMe(active, app);
        }
    }

    protected <I> Envelop invokeWrap(final I input) {
        if (input instanceof Envelop) {
            // Return Envelop directly
            return (Envelop) input;
        } else {
            // Return Envelop building
            return Envelop.success(input);
        }
    }

    /**
     * R method(T..)
     */
    protected Object invokeInternal(
        final Object proxy,
        final Method method,
        final Envelop envelop
    ) {
        // Preparing Method
        invokePre(method, envelop);
        // Return value here.
        return InvokerUtil.invokeCall(proxy, method, envelop);
    }

    /**
     *
     */
    protected <I> Function<I, Future<Envelop>> nextEnvelop(
        final Vertx vertx,
        final Method method) {
        return item -> this.nextEnvelop(vertx, method, item);
    }

    protected <T> Future<Envelop> nextEnvelop(
        final Vertx vertx,
        final Method method,
        final T result
    ) {
        final UddiClient client = Uddi.client(getClass());
        return client.bind(vertx).bind(method).connect(Ux.fromEnvelop(result));
    }
}
