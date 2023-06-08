package io.vertx.up.backbone.invoker;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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


        // LOG
        this.getLogger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass());


        // Get T
        final Class<?> tCls = returnType.getComponentType();
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
    public <I, O> void handle(final Object proxy, final Method method, final I input, final Handler<AsyncResult<O>> handler) {
        // Invoke directly
        final Envelop envelop = this.invokeWrap(input);
        // Future<T>
        final Class<?> returnType = method.getReturnType();


        // LOG
        this.getLogger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass());


        // Get T
        final Class<?> tCls = returnType.getComponentType();
        if (Envelop.class == tCls) {
            final Future<Envelop> result = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(result), _500ReturnNullException.class, getClass(), method);

            result.onComplete(item -> handler.handle(Future.succeededFuture((O) item.result())));
        } else {
            final Future result = Ut.invoke(proxy, method.getName(), envelop);

            // Null Pointer return value checking
            Fn.out(Objects.isNull(result), _500ReturnNullException.class, getClass(), method);
            handler.handle(result);
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


        // LOG
        this.getLogger().info(
            INFO.MSG_RPC,
            this.getClass(),
            returnType,
            method.getName(),
            method.getDeclaringClass()
        );


        if (Envelop.class == tCls) {
            // Execute Future<Envelop>
            final Future<Envelop> future = InvokerUtil.invoke(proxy, method, envelop);

            future.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        } else {
            final Future future = InvokerUtil.invoke(proxy, method, envelop);

            future.compose(this.nextEnvelop(vertx, method))
                .onComplete(Ux.handler(message));
        }
    }
}
