package io.vertx.up.backbone.invoker;

import io.vertx.core.*;
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
        // LOG
        this.getLogger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass()
        );


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
            // Null Pointer return value checking
            // Fn.out(Objects.isNull(returnValue), _500ReturnNullException.class, getClass(), method);
            if (null == returnValue) {
                final Promise promise = Promise.promise();
                promise.future().onComplete(Ux.handler(message));
            } else {

                final Future future = (Future) returnValue;
                future.onComplete(Ux.handler(message));
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public <I, O> void handle(final Object proxy, final Method method, final I input, final Handler<AsyncResult<O>> handler) {
        final Envelop envelop = this.invokeWrap(input);

        // Deserialization from message bus.
        final Class<?> returnType = method.getReturnType();


        // LOG
        this.getLogger().info(
            INFO.MSG_HANDLE,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass()
        );


        // Get T
        final Class<?> tCls = returnType.getComponentType();
        if (Envelop.class == tCls) {
            // Input type is Envelop, input directly
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(result), _500ReturnNullException.class, this.getClass(), method);

            result.onComplete(item -> handler.handle(Future.succeededFuture((O) item.result())));
            // result.setHandler(item -> message.reply(item.result()));
        } else {
            final Object returnValue = this.invokeInternal(proxy, method, envelop);
            // Null Pointer return value checking
            // Fn.out(Objects.isNull(returnValue), _500ReturnNullException.class, getClass(), method);
            if (null == returnValue) {
                handler.handle(Future.succeededFuture());
            } else {
                final Future future = (Future) returnValue;
                handler.handle(future);
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


        // LOG
        this.getLogger().info(
            INFO.MSG_RPC,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass()
        );


        // Get T
        final Class<?> tCls = returnType.getComponentType();
        if (Envelop.class == tCls) {
            // Input type is Envelop, input directly
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);
            result.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        } else {
            final Future future = this.invokeJson(proxy, method, envelop);
            future.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        }
    }
}
