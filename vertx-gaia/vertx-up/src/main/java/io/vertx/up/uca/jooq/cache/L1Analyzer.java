package io.vertx.up.uca.jooq.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class L1Analyzer {

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
                 * Definition compared with expected, they must be equal
                 */
                if (expectedCls != actualCls) {
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
}
