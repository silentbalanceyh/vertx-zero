package io.vertx.up.uca.invoker;

import io.vertx.core.Future;
import io.vertx.core.Promise;
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
 * Future<T> method(I)
 */
public class AsyncInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType, final Class<?> paramCls) {
        // Verify
        final boolean valid =
            (void.class != returnType && Void.class != returnType);
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    @SuppressWarnings("all")
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        final Envelop envelop = message.body();
        // Deserialization from message bus.
        final Class<?> returnType = method.getReturnType();
        this.getLogger().info(Info.MSG_FUTURE, this.getClass(), returnType, false);
        // Get T
        final Class<?> tCls = returnType.getComponentType();
        if (Envelop.class == tCls) {
            // Input type is Envelop, input directly
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(result), _500ReturnNullException.class, getClass(), method);

            result.onComplete(item -> message.reply(item.result()));
            // result.setHandler(item -> message.reply(item.result()));
        } else {
            final Object returnValue = this.invokeInternal(proxy, method, envelop);
            if (null == returnValue) {
                /*
                    final Future future = Future.future();
                    future.setHandler(Ux.handler(message));
                    Not frequent usage of this branch
                */
                final Promise promise = Promise.promise();
                // promise.future().setHandler(Ux.handler(message));
                promise.future().onComplete(Ux.handler(message));
            } else {
                // Null Pointer return value checking
                Fn.out(Objects.isNull(returnValue), _500ReturnNullException.class, getClass(), method);

                final Future future = (Future) returnValue;
                future.onComplete(Ux.handler(message));
                // future.setHandler(Ux.handler(message));
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        final Envelop envelop = message.body();
        // Deserialization from message bus.
        final Class<?> returnType = method.getReturnType();
        this.getLogger().info(Info.MSG_FUTURE, this.getClass(), returnType, true);
        // Get T
        final Class<?> tCls = returnType.getComponentType();
        if (Envelop.class == tCls) {
            // Input type is Envelop, input directly
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);
            /* replaced old cold
            result.compose(item -> TunnelClient.create(this.getClass())
                    .connect(vertx)
                    .connect(method)
                    .send(item))
                    .setHandler(Ux.handler(message)); */
            result.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        } else {
            final Future future = this.invokeJson(proxy, method, envelop);
            /* replaced old code
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
