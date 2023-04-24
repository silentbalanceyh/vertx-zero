package io.vertx.up.log;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.exception.heart.ErrorMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;

import java.text.MessageFormat;

/**
 *
 */
public final class Errors {

    private static final String ZERO_ERROR = "[ERR{0}] ({1}) Zero Error: {2}.";
    private static final String WEB_ERROR = "[ERR{0}] ({1}) Web Error: {2}.";

    public static String normalize(final Class<?> clazz,
                                   final int code,
                                   final Object... args) {
        return normalize(clazz, code, ZERO_ERROR, args);
    }

    public static String normalizeWeb(final Class<?> clazz,
                                      final int code,
                                      final Object... args) {
        return normalize(clazz, code, WEB_ERROR, args);
    }

    private static String normalize(final Class<?> clazz,
                                    final int code,
                                    final String tpl,
                                    final Object... args) {
        return Fn.orJvm(() -> {
            final String key = ("E" + Math.abs(code)).intern();
            final Node<JsonObject> node = Node.infix(KPlugin.ERROR);
            final JsonObject data = node.read();
            if (null != data && data.containsKey(key)) {
                // 1. Read pattern
                final String pattern = data.getString(key);
                // 2. Build message
                final String error = MessageFormat.format(pattern, args);
                // 3. Format
                return MessageFormat.format(
                    tpl, String.valueOf(code),
                    clazz.getSimpleName(),
                    error
                );
            } else {
                throw new ErrorMissingException(code, clazz.getName());
            }
        }, clazz);
    }

    public static String method(final Class<?> clazzPos,
                                final String methodPos) {
        final StackTraceElement[] methods = Thread.currentThread().getStackTrace();
        String methodName = null;
        int position = 0;
        for (int idx = 0; idx < methods.length; idx++) {
            final String clazz = methods[idx].getClassName();
            final String method = methods[idx].getMethodName();
            if (clazz.equals(clazzPos.getName())
                && method.equals(methodPos)) {
                position = idx + 1;
            }
        }
        if (position < methods.length - 1) {
            methodName = methods[position].getMethodName();
        }
        return methodName;
    }
}
