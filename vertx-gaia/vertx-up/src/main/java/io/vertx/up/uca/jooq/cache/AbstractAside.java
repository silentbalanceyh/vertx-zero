package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.tp.plugin.cache.hit.CacheMeta;
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
    protected transient JqAnalyzer analyzer;

    protected void initialize(final Class<?> clazz, final VertxDAO vertxDAO) {
        final JqAnalyzer analyzer = JqAnalyzer.create(vertxDAO);
        this.analyzer = analyzer;
        this.executor = new L1Aside(analyzer);
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
    protected <T> T readAsync(final CacheKey key, final CacheMeta meta, final ProceedingJoinPoint point) {
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
            logger().info("( Aop ) `{0}` aspecting... ( Async ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.readAsync(key, meta, () -> (Future) point.proceed(args));
        } else {
            /*
             * Sync calling
             */
            logger().info("( Aop ) `{0}` aspecting... ( Sync ) {1}", name, Ut.fromJoin(args));
            return (T) this.executor.read(key, meta, () -> (T) point.proceed(args));
        }
    }

    protected <T> T argument(final Integer index, final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        if (index < args.length) {
            return (T) args[index];
        } else {
            return null;
        }
    }

    protected CacheMeta metadata() {
        return new CacheMeta(this.analyzer.type()).primaryKey(this.analyzer.primarySet());
    }
}
