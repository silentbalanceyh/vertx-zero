package io.vertx.up.uca.invoker;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.InvokerNullException;

/**
 *
 */
public class JetSelector {

    private static final Annal LOGGER = Annal.get(JetSelector.class);

    public static Invoker select(final Class<?> returnType,
                                 final Class<?> paramCls) {
        // 1. Return void
        Invoker invoker = null;
        if (void.class == returnType || Void.class == returnType) {
            if (Envelop.class == paramCls) {
                // void method(Envelop)
                invoker = Ut.singleton(PingInvoker.class);
            } else if (Message.class.isAssignableFrom(paramCls)) {
                // void method(Message<Envelop>)
                invoker = Ut.singleton(MessageInvoker.class);
            }
        } else if (Envelop.class == returnType) {
            if (Envelop.class == paramCls) {
                // Envelop method(Envelop)
                // Rpc supported.
                invoker = Ut.singleton(SyncInvoker.class);
            }
        } else if (Future.class.isAssignableFrom(returnType)) {
            if (Envelop.class == paramCls) {
                // Future<T> method(Envelop)
                // Rpc supported.
                invoker = Ut.singleton(FutureInvoker.class);
            } else {
                // Future<T> method(I)
                // Rpc supported.
                invoker = Ut.singleton(AsyncInvoker.class);
            }
        } else {
            if (!Message.class.isAssignableFrom(paramCls)) {
                // Java direct type, except Message<T> / Envelop
                // T method(I)
                // Rpc supported.
                invoker = Ut.singleton(DynamicInvoker.class);
            }
        }
        Fn.outUp(null == invoker, LOGGER,
                InvokerNullException.class, JetSelector.class,
                returnType, paramCls);
        return invoker;
    }
}
