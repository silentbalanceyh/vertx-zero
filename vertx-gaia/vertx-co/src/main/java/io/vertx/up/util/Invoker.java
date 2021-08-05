package io.vertx.up.util;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.up.exception.zero.InvokingSpecException;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Call interface method by cglib
 */
@SuppressWarnings("unchecked")
final class Invoker {
    private Invoker() {
    }

    static <T> T invokeObject(
            final Object instance,
            final String name,
            final Object... args) {
        return Fn.getNull(() -> {
            final MethodAccess access = MethodAccess.get(instance.getClass());
            // Direct invoke, multi overwrite for unbox/box issue still existing.
            Object result;
            try {
                result = access.invoke(instance, name, args);
            } catch (final Throwable ex) {
                // Could not call, re-find the method by index
                // Search method by argument index because could not call directly
                final int index;
                final List<Class<?>> types = new ArrayList<>();
                for (final Object arg : args) {
                    types.add(Ut.toPrimary(arg.getClass()));
                }
                index = access.getIndex(name, types.toArray(new Class<?>[]{}));
                result = access.invoke(instance, index, args);
            }
            final Object ret = result;
            return Fn.getNull(() -> (T) ret, ret);
        }, instance, name);
    }

    static <T> Future<T> invokeAsync(final Object instance,
                                     final Method method,
                                     final Object... args) {
        /*
         * Analyzing method returnType first
         */
        final Class<?> returnType = method.getReturnType();
        try {
            /*
             * Void return for continue calling
             */
            if (void.class == returnType) {
                /*
                 * When void returned, here you must set the last argument to Future<T>
                 * Arguments [] + Future<T> future
                 *
                 * Critical:
                 * -- It means that if you want to return void.class, you must provide
                 *    argument future and let the future as last arguments
                 * -- The basic condition is
                 *    method declared length = args length + 1
                 */
                Fn.out(method.getParameters().length != args.length + 1,
                        InvokingSpecException.class, Invoker.class, method);
                /*
                 * void.class, the system should provide secondary parameters
                 */
                final Promise<T> promise = Promise.promise();
                final Object[] arguments = Ut.elementAdd(args, promise.future());
                method.invoke(instance, arguments);
                return promise.future();
            } else {
                final Object returnValue = method.invoke(instance, args);
                if (Objects.isNull(returnValue)) {
                    /*
                     * Future also null
                     * Return to Future.succeededFuture with null reference
                     * instead of use promise here.
                     */
                    return Future.succeededFuture(null);
                } else {
                    /*
                     * Workflow async invoking issue here for
                     * Code programming, it's very critical issue of Future compose
                     */
                    if (isEqualAnd(returnType, Future.class)) {
                        /*
                         * Future<T> returned directly,
                         * Connect future -> future
                         * Return to Future directly, because future is method
                         * return value, here, we could return internal future directly
                         * Replaced with method returnValue
                         */
                        return ((Future<T>) returnValue);
                    } else if (isEqualAnd(returnType, AsyncResult.class)) {
                        /*
                         * AsyncResult
                         */
                        final AsyncResult<T> async = (AsyncResult<T>) returnValue;
                        final Promise<T> promise = Promise.promise();
                        promise.handle(async);
                        return promise.future();
                    } else if (isEqualAnd(returnType, Handler.class)) {
                        /*
                         * Handler, not testing here.
                         * Connect future to handler
                         * Old code
                         * promise.future().setHandler(((Handler<AsyncResult<T>>) returnValue));
                         */
                        return ((Future<T>) returnValue);
                    } else {
                        /*
                         * Sync calling
                         * Wrapper future with T instance directly
                         */
                        final T returnT = (T) returnValue;
                        return Future.succeededFuture(returnT);
                    }
                }
            }
        } catch (final Throwable ex) {
            ex.printStackTrace();
            return Future.failedFuture(ex);
        }
        // Old code set to un-reach code here
        // return promise.future();
    }

    private static boolean isEqualAnd(final Class<?> clazz, final Class<?> interfaceCls) {
        return clazz == interfaceCls || Instance.isMatch(clazz, interfaceCls);
    }
}
