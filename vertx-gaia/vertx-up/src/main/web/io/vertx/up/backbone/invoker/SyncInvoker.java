package io.vertx.up.backbone.invoker;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

import java.lang.reflect.Method;

/**
 * Envelop method(Envelop)
 */
public class SyncInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType,
                       final Class<?> paramCls) {
        // Verify
        final boolean valid =
            Envelop.class == returnType && paramCls == Envelop.class;
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        // LOG
        this.getLogger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        // Invoke directly
        final Envelop envelop = message.body();
        message.reply(InvokerUtil.invoke(proxy, method, envelop));
    }

    @Override
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        // LOG
        this.getLogger().info(
            INFO.MSG_RPC,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        final Envelop envelop = message.body();
        final Envelop result = InvokerUtil.invoke(proxy, method, envelop);
        this.nextEnvelop(vertx, method, result)
            .onComplete(Ux.handler(message));
    }

    @Override
    public <I, O> void handle(final Object proxy, final Method method,
                              final I input, final Handler<AsyncResult<O>> handler) {
        // LOG
        this.getLogger().info(
            INFO.MSG_HANDLE,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );

        final Envelop envelop = this.invokeWrap(input);
        final Envelop result = InvokerUtil.invoke(proxy, method, envelop);
        final O extracted = result.data();
        handler.handle(Future.succeededFuture(extracted));
    }
}
