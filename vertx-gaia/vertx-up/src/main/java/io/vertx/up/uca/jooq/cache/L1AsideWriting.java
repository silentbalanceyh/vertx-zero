package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CMessage;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
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
            this.logger().info("( Aop ) `{0}` delete aspecting... ( Async ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.deleteAsync(messages, () -> (Future<R>) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            this.logger().info("( Aop ) `{0}` delete aspecting... ( Sync ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.delete(messages, () -> (R) point.proceed(args));
        }
    }
}
