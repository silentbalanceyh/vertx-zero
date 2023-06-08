package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.up.plugin.cache.hit.CMessage;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class L1AsideReading extends AbstractAside {
    /*
     * Async / Sync calling in uniform form here
     * The returned data type are following:
     * 1) T as entity
     * 2) List<T> as `java.util.List<T>` for entity collection
     * 3) Boolean as `checking/double checking` etc
     *
     * The workflow mode is as:
     * 1) T as sync.
     * 2) Future<T> as async.
     *
     * To avoid `java.util.EmptyStackException`, here I tried split method into `Async/Sync`
     * The old code logical is as follow of `readAsync` here, but because returned type is
     * -- Boolean
     * -- Future<Boolean>
     * Above two data type could not mapped to T uniform here
     */

    protected Future<Boolean> existAsync(final CMessage message, final ProceedingJoinPoint point) {
        /*
         * Get method definition
         */
        final Method method = L1Analyzer.method(point);
        final String name = method.getName();
        /*
         * Args of Object[]
         */
        final Object[] args = point.getArgs();
        /*
         * Async calling
         */
        this.logger().debug(INFO.AOP_EXIST_ASYNC, name, Ut.fromJoin(args));
        return this.executor(point).existAsync(message, () -> (Future<Boolean>) point.proceed(args));
    }

    protected Boolean existSync(final CMessage message, final ProceedingJoinPoint point) {
        /*
         * Get method definition
         */
        final Method method = L1Analyzer.method(point);
        final String name = method.getName();
        /*
         * Args of Object[]
         */
        final Object[] args = point.getArgs();
        /*
         * Sync calling
         */
        this.logger().debug(INFO.AOP_EXIST_SYNC, name, Ut.fromJoin(args));
        return this.executor(point).exist(message, () -> (Boolean) point.proceed(args));
    }

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
            this.logger().debug(INFO.AOP_READ_ASYNC, name, Ut.fromJoin(args));
            return (T) this.executor(point).readAsync(message, () -> (Future<T>) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            this.logger().debug(INFO.AOP_READ_SYNC, name, Ut.fromJoin(args));
            return (T) this.executor(point).read(message, () -> (T) point.proceed(args));
        }
    }
}
