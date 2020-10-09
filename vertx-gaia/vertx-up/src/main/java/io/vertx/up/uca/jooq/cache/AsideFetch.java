package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CacheCond;
import io.vertx.tp.plugin.cache.hit.CacheId;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Action for following APIs:
 */
@Aspect
@SuppressWarnings("all")
public class AsideFetch extends AbstractAside {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }

    /*
     * AOP definition as following for each APIs, here are calling chain definition for `fetchX`
     * fetchAllAsync()
     *      <-- fetchJAllAsync()
     *      <-- fetchJAllAsync(pojo)
     *
     * fetchAll()
     *      <-- fetchJAll()
     *      <-- fetchJAll(pojo)
     *
     * fetchAsync(String, Object)
     *      <-- fetchJAsync(String, Object)
     *      <-- fetchInAsync(String, Object...)
     *      <-- fetchInAsync(String, Collection)
     *      <-- fetchInAsync(String, JsonArray)
     *      <-- fetchJInAsync(String, Object...)
     *      <-- fetchJInAsync(String, Collection)
     *      <-- fetchJInAsync(String, JsonArray)
     * fetch(String, Object)
     *      <-- fetchJ(String, Object)
     *      <-- fetchIn(String, Object...)
     *      <-- fetchIn(String, Collection)
     *      <-- fetchIn(String, JsonArray)
     *      <-- fetchJIn(String, Object...)
     *      <-- fetchJIn(String, Collection)
     *      <-- fetchJIn(String, JsonArray)
     *
     * fetchAsync(JsonObject)
     *      <-- fetchJAsync(JsonObject)
     *      <-- fetchAndAsync(JsonObject)
     *      <-- fetchJAndAsync(JsonObject)
     *      <-- fetchOrAsync(JsonObject)
     *      <-- fetchJOrAsync(JsonObject)
     *
     * fetchAsync(JsonObject, pojo)
     *      <-- fetchJAsync(JsonObject, pojo)
     *      <-- fetchAndAsync(JsonObject, pojo)
     *      <-- fetchJAndAsync(JsonObject, pojo)
     *      <-- fetchOrAsync(JsonObject, pojo)
     *      <-- fetchJOrAsync(JsonObject, pojo)
     *
     * fetch(JsonObject)
     *      <-- fetchJ(JsonObject)
     *      <-- fetchAnd(JsonObject)
     *      <-- fetchJAnd(JsonObject)
     *      <-- fetchOr(JsonObject)
     *      <-- fetchJOr(JsonObject)
     *
     * fetch(JsonObject, pojo)
     *      <-- fetchJ(JsonObject, pojo)
     *      <-- fetchAnd(JsonObject, pojo)
     *      <-- fetchJAnd(JsonObject, pojo)
     *      <-- fetchOr(JsonObject, pojo)
     *      <-- fetchJOr(JsonObject, pojo)
     *
     *
     * fetchByIdAsync(Object)
     *      <-- fetchJByIdAsync(Object)
     *
     * fetchById(Object)
     *      <-- fetchJById(Object)
     *
     * fetchOneAsync(String, Object)
     *      <-- fetchJOneAsync(String, Object)
     *
     * fetchOne(String, Object)
     *      <-- fetchJOne(String, Object)
     *
     * fetchOneAsync(JsonObject)
     *      <-- fetchJOneAsync(JsonObject)
     *
     * fetchOne(JsonObject)
     *      <-- fetchJOne(JsonObject)
     *
     * fetchOneAsync(JsonObject, pojo)
     *      <-- fetchJOneAsync(JsonObject, pojo)
     *
     * fetchOne(JsonObject, pojo)
     *      <-- fetchJOne(JsonObject, pojo)
     *
     * The rule for this aside is as:
     *
     * 1. fetchAll will be ignored and the cache is not support.
     * 2. Only the first level method should be cached, all `<--` will be ignored.
     * 3. The final results is:
     * -- fetch
     * -- fetchAsync
     * -- fetchById
     * -- fetchByIdAsync
     * -- fetchOne
     * -- fetchOneAsync
     */

    /*
     * fetch
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetch(..))")
    public <T> T fetch(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, String.class, Object.class)) {
            /*
             * fetch(String,Object)
             */
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetch(JsonObject)
             */
        } else if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
            /*
             * fetch(JsonObject, String)
             */
        }
        return null;
    }

    /*
     * fetchAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchAsync(..))")
    public <T> T fetchAsync(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, String.class, Object.class)) {
            /*
             * fetchAsync(String,Object)
             */
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetchAsync(JsonObject)
             */
        } else if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
            /*
             * fetchAsync(JsonObject, String)
             */
        }
        return null;
    }

    /*
     * 「Finished」
     * fetchById
     * fetchByIdAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchById*(..)) && args(id)", argNames = "id")
    public <T> T fetchById(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Returned Type checked only, two signatures
         */
        final CacheKey key = new CacheId(id);
        return this.readAsync(key, this.metadata(), point);
    }

    /*
     * fetchOne
     * fetchOneAsync
     * Becareful about two signature definitions here
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.fetchOne*(..))")
    public <T> T fetchOne(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, String.class, Object.class)) {
            /*
             * fetchOne(String,Object)
             * fetchOneAsync(String,Object)
             */
            final String field = this.argument(0, point);
            final Object value = this.argument(1, point);
            final CacheKey key = new CacheCond(field, value);
            return this.readAsync(key, this.metadata(field, value), point);
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetchOne(JsonObject)
             * fetchOneAsync(JsonObject)
             */
            final JsonObject condition = this.argument(0, point);
            final CacheKey key = new CacheCond(condition);
            return this.readAsync(key, this.metadata(condition), point);
        } else if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
            /*
             * fetchOne(JsonObject,String)
             * fetchOneAsync(JsonObject, String)
             * POJO mode processing
             */
        }
        return (T) point.proceed();
    }
}
