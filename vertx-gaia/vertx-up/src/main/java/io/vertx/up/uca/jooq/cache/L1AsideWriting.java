package io.vertx.up.uca.jooq.cache;

import io.vertx.tp.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1AsideWriting extends AbstractAside {
    /*
     * Async / Sync calling in uniform form here
     *
     */
    protected <T> T deleteAsync(final List<CMessage> messages, final ProceedingJoinPoint point) {
        /*
         * Get method definition
         */
        final Method method = L1Analyzer.method(point);
        /*
         * Class<?>, returnType
         */
        final Class<?> returnType = method.getReturnType();
        final String name = method.getName();
        /*
         * Args of Object[]
         */
        final Object[] args = point.getArgs();

        return null;
    }
}
