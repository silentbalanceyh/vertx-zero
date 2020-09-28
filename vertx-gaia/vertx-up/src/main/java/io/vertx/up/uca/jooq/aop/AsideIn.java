package io.vertx.up.uca.jooq.aop;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CacheId;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.util.Ut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;

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
    private transient L1Aside executor;

    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        this.executor = new L1Aside(JqAnalyzer.create(dao));
    }

    /*
     * Around for find
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchById*(..)) && args(id)", argNames = "id")
    public <T, K> T findById(final ProceedingJoinPoint point, final K id) throws Throwable {
        final CacheKey key = new CacheId(id);
        return execAsync(key, point, "fetchById");
    }

    /*
     * search / searchAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.search*(..))")
    public <T> T search(final ProceedingJoinPoint point) throws Throwable {
        final CacheKey key = AsideDim.keyCond(point);
        return execAsync(key, point, "search");
    }

    /*
     * existById / existByIdAsync
     * missById / missByIdAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existById*(..)) && args(id)", argNames = "id")
    public <T, K> T existById(final ProceedingJoinPoint point, final K id) throws Throwable {
        final CacheKey key = new CacheId(id);
        return execAsync(key, point, "existById");
    }

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.missById*(..)) && args(id)", argNames = "id")
    public <T, K> T missById(final ProceedingJoinPoint point, final K id) throws Throwable {
        final CacheKey key = new CacheId(id);
        return execAsync(key, point, "missById");
    }

    /*
     * Around for fetch
     * 1) fetchOne(JsonObject)
     *    fetchOne(String, Object)
     * 2) fetchOneAsync(JsonObject)
     *    fetchOneAsync(String, Object)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchOne*(..))")
    public <T> T fetchOne(final ProceedingJoinPoint point) throws Throwable {
        final CacheKey key = AsideDim.keyCond(point);
        return execAsync(key, point, "fetchOne");
    }

    /*
     * Distinguish with fetchOne, here could not use `fetch*` pattern
     * The call tracking is as following:
     *
     * 1) fetchAll ( No Cache )
     * 2) fetchAnd / fetchOr / fetchIn: These all methods will call `fetch/fetchAsync` API instead
     * 3) Only cached `fetch / fetchAsync` for all API here
     * 4) exist / existAsync / miss / missAsync will call `fetch/fetchAsync` API instead
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetch(..))")
    public <T> List<T> fetch(final ProceedingJoinPoint point) throws Throwable {
        final CacheKey key = AsideDim.keyCond(point);
        return execAsync(key, point, "fetch");
    }

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchAsync(..))")
    public <T> Future<List<T>> fetchAsync(final ProceedingJoinPoint point) throws Throwable {
        final CacheKey key = AsideDim.keyCond(point);
        return execAsync(key, point, "fetchAsync");
    }

    // ----------------------- Could not be modified ----------------------

    private <T> T execAsync(final CacheKey key, final ProceedingJoinPoint point, final String method) {
        final Class<?> returnType = AsideDim.returnType(point);
        if (Future.class == returnType) {
            if (Ut.notNil(method)) {
                LOGGER.info("( Aop ) `{0}Async(Object)` aspecting.. (Async) {1}", method, Ut.fromJoin(point.getArgs()));
            }
            return (T) this.executor.readAsync(key, () -> (Future) point.proceed(point.getArgs()));
        } else {
            if (Ut.notNil(method)) {
                LOGGER.info("( Aop ) `{0}(Object)` aspecting.. (Sync) {1}", method, Ut.fromJoin(point.getArgs()));
            }
            return this.executor.read(key, () -> (T) point.proceed(point.getArgs()));
        }
    }
}
