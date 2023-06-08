package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.up.plugin.cache.hit.CMessage;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class L1AsideWriting extends AbstractAside {
    /*
     * Async / Sync calling in uniform form here
     *
     */
    protected <T, R> T writeAsync(final List<CMessage> messages, final ProceedingJoinPoint point) {
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
        if (Future.class == returnType) {
            /*
             * Async calling
             */
            this.logger().debug(INFO.AOP_WRITE_ASYNC, name, Ut.fromJoin(args));
            return (T) this.executor(point).deleteAsync(messages, () -> (Future<R>) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            this.logger().debug(INFO.AOP_WRITE_SYNC, name, Ut.fromJoin(args));
            return (T) this.executor(point).delete(messages, () -> (R) point.proceed(args));
        }
    }
}
