package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractAside {
    /*
     * L1 Aside executing for cache
     */
    protected transient L1Aside executor;

    protected void initialize(final Class<?> clazz, final VertxDAO vertxDAO) {
        this.executor = new L1Aside(JqAnalyzer.create(vertxDAO));
    }
    /*
     * Method / Return Type
     */

    /*
     * Logger that will be used in L1 cache sub-classes
     */
    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /*
     * Executing method processing
     */
    protected <T> T readAsync(final CacheKey key, final ProceedingJoinPoint point) {
        /*
         * Get method definition
         */
        final Method method = L1Condition.method(point);
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
            logger().info("( Aop ) `{0}` aspecting... ( Async ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.readAsync(key, () -> (Future) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            logger().info("( Aop ) `{0}` aspecting... ( Sync ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.read(key, () -> (T) point.proceed(args));
        }
    }
}
