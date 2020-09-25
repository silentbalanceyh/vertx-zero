package io.vertx.up.uca.jooq.aop;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.up.uca.jooq.JqAnalyzer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * The design interface for cache usage here, it's Cache-Aside pattern, here we defined:
 * 1) SoR: System of Record, it's database of MySQL based on Jooq
 * 2) Cache: The cache database here, it's depend on Harp component in `vertx-cache.xml`
 * 3) Callback: When the request could not hit cache here, back to SoR
 *
 * Here we used Cache-Aside for cache hitting
 *
 * 1) The Read: Get -> Null Check -> Write
 * 2) The Write: Get Key -> Delete in Cache ( The next time the system will refresh cache )
 */
@Aspect
@SuppressWarnings("all")
public class JqCache {
    private transient JqL1 l1;

    /*
     * Aspect in constructor for input of
     * -- Class<T> : The dao class for current operation
     * -- VertxDao : The bind vertx dao that contains vertx reference
     */
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        /*
         * L1 Cache enabled
         * 1. VertxDao
         * 2. JqAnalyzer reference of object
         *
         * Here processed L1 cache building for different method
         */
        this.l1 = JqL1.create(dao, JqAnalyzer.create(dao));
    }

    /*
     * Around for read
     * 1) findById(Object)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.Ux*.findById(Object)) && args(input)", argNames = "input")
    public <T> T findById(final ProceedingJoinPoint point,
                          final T input) throws Throwable {
        final Object target = point.getTarget();
        return (T) point.proceed();
    }
}
