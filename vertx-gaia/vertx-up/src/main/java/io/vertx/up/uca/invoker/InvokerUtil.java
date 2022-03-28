package io.vertx.up.uca.invoker;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.uca.serialization.TypedArgument;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.AsyncSignatureException;
import io.vertx.zero.exception.WorkerArgumentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Tool for invoker do shared works.
 */
@SuppressWarnings("all")
public class InvokerUtil {
    private static final Annal LOGGER = Annal.get(InvokerUtil.class);

    public static <T> T invoke(final Object proxy, final Method method, final Object... args) {
        /*
         * Be sure to trust args first calling and then normalized calling
         * by `Ut.invoke`, because `Ut.invoke` will parse many parameters here, it means that
         * it will analyze the metadata information in runtime, I think it's not needed in
         * zero framework now. the method could be invoked with args directly.
         */
        try {
            return (T) method.invoke(proxy, args);
        } catch (final InvocationTargetException | IllegalAccessException ex) {
            return Ut.invoke(proxy, method.getName(), args);
        }
    }

    /**
     * Whether this method is void
     *
     * @param method checked method
     *
     * @return checked result
     */
    static boolean isVoid(final Method method) {
        final Class<?> returnType = method.getReturnType();
        return void.class == returnType || Void.class == returnType;
    }

    /**
     * TypedArgument verification
     * Public for replacing duplicated code
     *
     * @param method checked method.
     * @param target checked class
     */
    public static void verifyArgs(final Method method,
                                  final Class<?> target) {

        // 1. Ensure method length
        final Class<?>[] params = method.getParameterTypes();
        final Annal logger = Annal.get(target);
        // 2. The parameters
        Fn.outUp(Values.ZERO == params.length,
            logger, WorkerArgumentException.class,
            target, method);
    }

    static void verify(
        final boolean condition,
        final Class<?> returnType,
        final Class<?> paramType,
        final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outUp(condition, logger,
            AsyncSignatureException.class, target,
            returnType.getName(), paramType.getName());
    }

    private static Object getValue(final Class<?> type,
                                   final Envelop envelop,
                                   final Supplier<Object> defaultSupplier) {
        // Multi calling for Session type
        final Object value;
        if (Session.class == type) {
            /*
             * RBAC required ( When Authenticate )
             * 1) Provide username / password to get data from remote server.
             * 2) Request temp authorization code ( Required Session ).
             */
            value = envelop.session();
        } else {
            value = defaultSupplier.get();
            final Object argument = null == value ? null : ZeroSerializer.getValue(type, value.toString());
        }
        return value;
    }

    static Object invokeMulti(final Object proxy,
                              final Method method,
                              final Envelop envelop) {
        /*
         * One type dynamic here
         */
        final Object reference = envelop.data();
        /*
         * Non Direct
         */
        final Object[] arguments = new Object[method.getParameterCount()];
        final JsonObject json = (JsonObject) reference;
        final Class<?>[] types = method.getParameterTypes();
        /*
         * Adjust argument index
         */
        int adjust = 0;
        for (int idx = 0; idx < types.length; idx++) {
            /*
             * Multi calling for Session type
             */
            final Class<?> type = types[idx];
            /*
             * Found typed here
             * Adjust idx  - 1 to move argument index to
             * left.
             * {
             *    "0": "key",
             *    "1": "type",
             * }
             * (String,<T>,String) -> (idx, current), (0, 0), (1, ?), (2, 1)
             *                                               adjust = 1
             *
             * (<T>, String, String) -> (idx, current), (0, ?), (1, 0), (2, 1)
             *                                          adjust = 1
             *
             * (String, String,<T>) -> (idx, current), (0, 0), (1, 1), (2, ?)
             *                                                          adjust = 1
             */
            final Object analyzed = TypedArgument.analyzeWorker(envelop, type);
            if (Objects.isNull(analyzed)) {
                final int current = idx - adjust;
                final Object value = json.getValue(String.valueOf(current));
                if (Objects.isNull(value)) {
                    /*
                     * Input is null when type is not match, if type is JsonObject
                     * The result should be json instead of `null`
                     */
                    if (JsonObject.class == type && Values.IDX == idx) {
                        /*
                         * Here are often the method as
                         * method(JsonObject, ...) format
                         */
                        arguments[idx] = json.copy();
                    } else {
                        arguments[idx] = null;
                    }
                } else {
                    /*
                     * Serialization
                     */
                    arguments[idx] = ZeroSerializer.getValue(type, value.toString());
                }
            } else {
                /*
                 * Typed successfully
                 */
                arguments[idx] = analyzed;
                adjust += 1;
            }
        }
        return invoke(proxy, method, arguments);
        // return Ut.invoke(proxy, method.getName(), arguments);
    }

    static Object invokeSingle(final Object proxy,
                               final Method method,
                               final Envelop envelop) {
        final Class<?> argType = method.getParameterTypes()[Values.IDX];
        // Append single argument
        final Object analyzed = TypedArgument.analyzeWorker(envelop, argType);
        if (Objects.isNull(analyzed)) {
            // One type dynamic here
            final Object reference = envelop.data();
            // Non Direct
            Object parameters = reference;
            if (JsonObject.class == reference.getClass()) {
                final JsonObject json = (JsonObject) reference;
                if (TypedArgument.modeInterface(json)) {
                    // Proxy mode
                    if (Values.ONE == json.fieldNames().size()) {
                        // New Mode for direct type
                        parameters = json.getValue("0");
                    }
                }
            }
            final Object arguments = ZeroSerializer.getValue(argType, Ut.toString(parameters));
            return invoke(proxy, method, arguments); // Ut.invoke(proxy, method.getName(), arguments);
        } else {
            /*
             * XHeader
             * User
             * Session
             * These three argument types could be single
             */
            return invoke(proxy, method, analyzed); // Ut.invoke(proxy, method.getName(), analyzed);
        }
    }
}
