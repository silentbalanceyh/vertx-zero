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
class AsideDim {

    static Class<?> returnType(final ProceedingJoinPoint point) {
        final MethodSignature signature = (MethodSignature) point.getSignature();
        final Method method = signature.getMethod();
        return method.getReturnType();
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
