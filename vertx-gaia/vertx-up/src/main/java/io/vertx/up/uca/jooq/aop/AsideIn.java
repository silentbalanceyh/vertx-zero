package io.vertx.up.uca.jooq.aop;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.up.log.Annal;
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
 * This aop is for reading data
 */
@Aspect
@SuppressWarnings("all")
public class AsideIn {
    private static final Annal LOGGER = Annal.get(AsideIn.class);
    private transient L1Fetch fetcher;

    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        this.fetcher = new L1Fetch(JqAnalyzer.create(dao));
    }

    /*
     * Around for find
     * 1) findById(Object)
     * 2) findByIdAsync(Object)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.Ux*.fetchById*(..)) && args(id)", argNames = "id")
    public <T, K> T findById(final ProceedingJoinPoint point,
                             final K id) throws Throwable {
        final Class<?> returnType = AsideDim.returnType(point);
        if (Future.class == returnType) {
            LOGGER.info("( Aop ) `fetchByIdAsync(Object)` aspecting.. (Async) {0}", id);
            return (T) this.fetcher.findByIdAsync(id, () -> (Future) point.proceed());
        } else {
            LOGGER.info("( Aop ) `fetchById(Object)` aspecting.. (Sync) {0}", id);
            return this.fetcher.findById(id, () -> (T) point.proceed());
        }
    }

    /*
     * Around for fetch
     * 1) fetchOne
     * 2) fetchOneAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.Ux*.fetchOne*(..))")
    public <T> T fetchOne(final ProceedingJoinPoint point) throws Throwable {
        final int length = point.getArgs().length;
        System.out.println(length);
        return (T) point.proceed();
    }
}
