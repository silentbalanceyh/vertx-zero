package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CMessage;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public abstract class AbstractAsideReading extends AbstractAside {

    /*
     * Executing method processing
     */
    protected <T> T readAsync(final CMessage message, final ProceedingJoinPoint point) {
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
            this.logger().info("( Aop ) `{0}` aspecting... ( Async ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.readAsync(message, () -> (Future) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            this.logger().info("( Aop ) `{0}` aspecting... ( Sync ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.read(message, () -> (T) point.proceed(args));
        }
    }

    protected <T> T existAsync(final CMessage message, final ProceedingJoinPoint point) {
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
            this.logger().info("( Aop ) `{0}` aspecting... ( Async ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.existAsync(message, () -> (Future<Boolean>) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            this.logger().info("( Aop ) `{0}` aspecting... ( Sync ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.exist(message, () -> (Boolean) point.proceed(args));
        }
    }
}
