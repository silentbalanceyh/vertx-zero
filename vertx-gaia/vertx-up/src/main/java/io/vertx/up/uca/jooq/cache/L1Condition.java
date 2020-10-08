package io.vertx.up.uca.jooq.cache;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CacheCond;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Condition {

    static Method method(final ProceedingJoinPoint point) {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getMethod();
    }

    static boolean isMatch(final ProceedingJoinPoint point, final Class<?>... expected) {
        final Method method = method(point);
        final Class<?>[] clazz = method.getParameterTypes();
        /*
         * The expected and clazz must be matched
         */
        if (expected.length == clazz.length) {
            final int length = expected.length;
            boolean matched = true;
            for (int idx = 0; idx < length; idx++) {
                final Class<?> expectedCls = expected[idx];
                final Class<?> actualCls = clazz[idx];
                /*
                 * If they are equal ?
                 */
                if (expectedCls == actualCls) {
                    matched = false;
                    break;
                }
            }
            return matched;
        } else {
            /*
             * Do not match
             */
            return false;
        }
    }

    @SuppressWarnings("all")
    static CacheKey keyCond(final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        final int length = args.length;
        final CacheKey key;
        if (2 == length) {
            final Object field = (String) args[0];
            final Object value = args[1];
            if (field instanceof JsonObject) {
                /*
                 * JsonObject, pojo
                 */
                key = new CacheCond((JsonObject) field);
            } else {
                /*
                 * field, value
                 */
                key = new CacheCond((String) field, value);
            }
        } else {
            /*
             * JsonObject
             */
            final JsonObject condition = (JsonObject) args[0];
            key = new CacheCond(condition);
        }
        return key;
    }
}
