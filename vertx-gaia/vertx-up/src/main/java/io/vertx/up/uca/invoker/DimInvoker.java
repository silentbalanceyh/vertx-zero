package io.vertx.up.uca.invoker;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

import java.lang.reflect.Method;

public class DimInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType, final Class<?> paramCls) {
        // Verify
        final boolean valid =
                (void.class != returnType && Void.class != returnType);
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        final Envelop envelop = message.body();
        this.getLogger().info(Info.MSG_FUTURE, this.getClass(), method.getReturnType(), false);
        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        // The returnValue type could not be Future
        message.reply(returnValue);
    }

    @Override
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        final Envelop envelop = message.body();
        this.getLogger().info(Info.MSG_FUTURE, this.getClass(), method.getReturnType(), true);
        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        this.nextEnvelop(vertx, method, returnValue)
                .onComplete(Ux.handler(message));
    }
}
