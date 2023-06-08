package io.vertx.up.backbone.invoker;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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
        // Log
        this.getLogger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        final Envelop envelop = message.body();
        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        // The returnValue type could not be Future
        message.reply(returnValue);
    }

    @Override
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        // Log
        this.getLogger().info(
            INFO.MSG_RPC,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        final Envelop envelop = message.body();
        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        this.nextEnvelop(vertx, method, returnValue)
            .onComplete(Ux.handler(message));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, O> void handle(final Object proxy, final Method method,
                              final I input, final Handler<AsyncResult<O>> handler) {
        // Log
        this.getLogger().info(
            INFO.MSG_HANDLE,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        // 「Sync」
        final Envelop normalized = this.invokeWrap(input);
        final Object returnValue = this.invokeInternal(proxy, method, normalized);
        handler.handle(Future.succeededFuture((O) returnValue));
    }
}
