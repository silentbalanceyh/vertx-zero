package io.vertx.up.uca.jooq.aop;

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
}
