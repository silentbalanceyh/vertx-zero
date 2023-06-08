package io.vertx.up.uca.jooq.cache;

import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Action for following APIs:
 */
@Aspect
@SuppressWarnings("all")
public class AsideFetch extends L1AsideReading {
/*    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }*/

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
            final CMessage message = this.messageListField(point);
            return this.readAsync(message, point);
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetch(JsonObject)
             */
            final CMessage message = this.messageListCond(point);
            return this.readAsync(message, point);
        } else {
            if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
                /*
                 * 「POJO」
                 * fetch(JsonObject, String)
                 */
                final CMessage message = this.messageListPojo(point);
                return this.readAsync(message, point);
            }
            return (T) point.proceed(point.getArgs());
        }
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
            final CMessage message = this.messageListField(point);
            return this.readAsync(message, point);
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetchAsync(JsonObject)
             */
            final CMessage message = this.messageListCond(point);
            return this.readAsync(message, point);
        } else {
            if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
                /*
                 * 「POJO」
                 * fetchAsync(JsonObject, String)
                 */
                final CMessage message = this.messageListPojo(point);
                return this.readAsync(message, point);
            }
            return (T) point.proceed(point.getArgs());
        }
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
        final CMessage message = this.messageKey(id, point);
        return this.readAsync(message, point);
    }

    /*
     * 「Finished」
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
            final CMessage message = this.messageUniqueField(point);
            return this.readAsync(message, point);
        } else if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * fetchOne(JsonObject)
             * fetchOneAsync(JsonObject)
             */
            final CMessage message = this.messageUniqueCond(point);
            return this.readAsync(message, point);
        } else {
            if (L1Analyzer.isMatch(point, JsonObject.class, String.class)) {
                /*
                 * 「POJO」
                 * fetchOne(JsonObject,String)
                 * fetchOneAsync(JsonObject, String)
                 * 「Important」
                 * In current version, Pojo mode is for old system, it will be removed in future,
                 * so it does not support cache feature here.
                 */
                final CMessage message = this.messageUniquePojo(point);
                return this.readAsync(message, point);
            }
            return (T) point.proceed(point.getArgs());
        }
    }
}
