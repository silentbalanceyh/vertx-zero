package io.vertx.up.backbone.invoker;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.zero.exception.InvokerNullException;

/**
 *
 */
public class JetSelector {

    private static final Annal LOGGER = Annal.get(JetSelector.class);

    public static Invoker invoker(final Class<?> returnType,
                                  final Class<?> paramCls) {
        Invoker invoker = null;
        if (void.class == returnType || Void.class == returnType) {

            /*
             * 「Async Support」
             * Method return type is: void/Void
             * It means that you must implement internal async operation in
             * the method programming.
             */
            if (Envelop.class == paramCls) {
                // void method(Envelop)
                invoker = CACHE.CCT_INVOKER.pick(PingInvoker::new, PingInvoker.class.getName()); // Ut.?ingleton(PingInvoker.class);
            } else if (Message.class.isAssignableFrom(paramCls)) {
                // void method(Message<Envelop>)
                invoker = CACHE.CCT_INVOKER.pick(MessageInvoker::new, MessageInvoker.class.getName()); // Ut.?ingleton(MessageInvoker.class);
            } else {
                // void method(T)
                invoker = CACHE.CCT_INVOKER.pick(PingTInvoker::new, PingTInvoker.class.getName()); // Ut.?ingleton(PingTInvoker.class);
            }
        } else if (Envelop.class == returnType) {


            /*
             * 「Sync Only」
             * Method return type is: Envelop
             * This operation of method is sync operation definition, you can not
             * do any async operation in this kind of mode
             */
            if (Envelop.class == paramCls) {
                // Envelop method(Envelop)
                // Rpc supported.
                invoker = CACHE.CCT_INVOKER.pick(SyncInvoker::new, SyncInvoker.class.getName()); // Ut.?ingleton(SyncInvoker.class);
            } else {
                // Envelop method(I)
                invoker = CACHE.CCT_INVOKER.pick(DimInvoker::new, DimInvoker.class.getName()); // Ut.?ingleton(DimInvoker.class);
            }
        } else if (Future.class.isAssignableFrom(returnType)) {


            /*
             * 「Async Only」
             * Method return type is: Future
             * This operation of method is async operation definition, you can not
             * do any sync operation in this kind of mode
             */
            if (Envelop.class == paramCls) {
                // Future<T> method(Envelop)
                // Rpc supported.
                invoker = CACHE.CCT_INVOKER.pick(FutureInvoker::new, FutureInvoker.class.getName()); // Ut.?ingleton(FutureInvoker.class);
            } else {
                // Future<T> method(I)
                // Rpc supported.
                invoker = CACHE.CCT_INVOKER.pick(AsyncInvoker::new, AsyncInvoker.class.getName()); // Ut.?ingleton(AsyncInvoker.class);
            }
        } else {


            /*
             * 「Freedom」
             * Freedom mode is standard java specification method here, but in this kind of
             * mode, the framework remove three situations:
             * 1. Return = void/Void
             * 2. Return = Envelop
             * 3. Return = Future
             *
             * Also when you process this kind of method definition, the `Message` could not
             * be used, if you want to do Async operation, you can select `Future` returned type
             * as code major style in zero framework, it's recommend
             */
            if (!Message.class.isAssignableFrom(paramCls)) {
                // Java direct type, except Message<T> / Envelop
                // T method(I)
                // Rpc supported.
                invoker = CACHE.CCT_INVOKER.pick(DynamicInvoker::new, DynamicInvoker.class.getName()); // Ut.?ingleton(DynamicInvoker.class);
            }
        }
        Fn.outBoot(null == invoker, LOGGER,
            InvokerNullException.class, JetSelector.class,
            returnType, paramCls);
        return invoker;
    }
}
