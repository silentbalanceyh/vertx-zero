package io.vertx.up.uca.invoker;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._501RpcRejectException;

import java.lang.reflect.Method;

/**
 * void method(Envelop)
 */
public class PingTInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType,
                       final Class<?> paramCls) {
        final boolean valid = (void.class == returnType || Void.class == returnType);
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        // Invoke directly
        final Envelop envelop = message.body();
        this.invokeInternal(proxy, method, envelop);
        message.reply(Envelop.success(Boolean.TRUE));
    }

    @Override
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        // Return void is reject by Rpc continue
        throw new _501RpcRejectException(this.getClass());
    }
}
