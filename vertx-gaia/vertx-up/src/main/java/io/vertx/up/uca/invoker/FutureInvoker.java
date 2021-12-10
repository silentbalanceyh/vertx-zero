package io.vertx.up.uca.invoker;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._500ReturnNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Future<Envelop> method(Envelop)
 */
@SuppressWarnings("all")
public class FutureInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType,
                       final Class<?> paramCls) {
        // Verify
        final boolean valid =
            Future.class.isAssignableFrom(returnType) && paramCls == Envelop.class;
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        // Invoke directly
        final Envelop envelop = message.body();
        // Future<T>
        final Class<?> returnType = method.getReturnType();
        // Get T
        final Class<?> tCls = returnType.getComponentType();
        this.getLogger().info(Info.MSG_DIRECT, this.getClass(), returnType,
            method.getName(), method.getDeclaringClass());
        if (Envelop.class == tCls) {
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(result), _500ReturnNullException.class, getClass(), method);

            result.onComplete(item -> message.reply(item.result()));
        } else {
            final Future tResult = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(tResult), _500ReturnNullException.class, getClass(), method);

            tResult.onComplete(Ux.handler(message));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        // Invoke directly
        final Envelop envelop = message.body();
        // Future<T>
        final Class<?> returnType = method.getReturnType();
        // Get T
        final Class<?> tCls = returnType.getComponentType();
        this.getLogger().info(Info.MSG_RPC, this.getClass(), returnType,
            method.getName(), method.getDeclaringClass());
        if (Envelop.class == tCls) {
            // Execute Future<Envelop>
            final Future<Envelop> future = InvokerUtil.invoke(proxy, method, envelop); // Ut.invoke(proxy, method.getName(), envelop);
            /*
            future.compose(item -> TunnelClient.create(this.getClass())
                    .connect(vertx)
                    .connect(method)
                    .send(item))
                    .setHandler(Ux.handler(message)); */
            future.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        } else {
            final Future future = InvokerUtil.invoke(proxy, method, envelop); // Ut.invoke(proxy, method.getName(), envelop);
            /*
            future.compose(item -> TunnelClient.create(this.getClass())
                    .connect(vertx)
                    .connect(method)
                    .send(Ux.to(item)))
                    .compose(item -> Future.succeededFuture(Ux.to(item)))
                    .setHandler(Ux.handler(message)); */
            future.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        }
    }
}
