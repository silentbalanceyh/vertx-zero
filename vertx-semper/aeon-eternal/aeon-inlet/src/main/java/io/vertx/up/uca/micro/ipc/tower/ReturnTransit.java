package io.vertx.up.uca.micro.ipc.tower;

import io.vertx.core.Future;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._500RpcTransitInvokeException;
import io.zero.spec.function.ExceptionSupplier;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.lang.reflect.Method;

/**
 * Different return value to build Future<Envelop>
 */
class ReturnTransit {

    private static final Annal LOGGER = Annal.get(ReturnTransit.class);

    private static Object invoke(final ExceptionSupplier<Object> invokedSupplier,
                                 final Class<?> target,
                                 final Method method) {
        final Object returnValue;
        try {
            returnValue = invokedSupplier.get();
        } catch (final Throwable ex) {
            ex.printStackTrace();
            throw new _500RpcTransitInvokeException(target, method, ex);
        }
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    static Future<Envelop> build(final ExceptionSupplier<Object> supplier,
                                 final Class<?> target,
                                 final Method method) {
        final Object returnValue = invoke(supplier, target, method);
        if (null == returnValue) {
            // Empty Future
            return Future.succeededFuture(Envelop.ok());
        }
        final Class<?> clazz = returnValue.getClass();
        final Future<Envelop> result;
        if (Future.class.isAssignableFrom(clazz)) {
            // Fix Async Server Issue
            final Class<?> tCls = clazz.getComponentType();
            if (Envelop.class == tCls) {
                // Future<Envelop>
                LOGGER.info(Info.MSG_FLOW, "Future<Envelop>", clazz);
                result = (Future<Envelop>) returnValue;
            } else {
                // Future<JsonObject> or Future<JsonArray>
                LOGGER.info(Info.MSG_FLOW, "Future<T>", clazz);
                final Future future = (Future) returnValue;
                return future.compose(item -> Future.succeededFuture(Ux.fromEnvelop(item)));
            }
        } else {
            if (Envelop.class == clazz) {
                // Envelop got
                LOGGER.info(Info.MSG_FLOW, "Envelop", clazz);
                result = Future.succeededFuture((Envelop) returnValue);
            } else {
                LOGGER.info(Info.MSG_FLOW, "Other Type", clazz);
                final Envelop envelop = Envelop.success(returnValue);
                result = Future.succeededFuture(envelop);
            }
        }
        return result;
    }
}
