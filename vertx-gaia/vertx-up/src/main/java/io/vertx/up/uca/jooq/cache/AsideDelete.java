package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Empty aspect for placeholder here
 */
@Aspect
@SuppressWarnings("all")
public class AsideDelete extends L1AsideWriting {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
    /*
     * delete(T)
     *      <-- delete(JsonObject)
     *      <-- delete(JsonObject, pojo)
     *      <-- deleteJ(T)
     *      <-- deleteJ(JsonObject)
     *      <-- deleteJ(JsonObject, pojo)
     *
     * deleteAsync(T)
     *      <-- deleteAsync(JsonObject)
     *      <-- deleteAsync(JsonObject, pojo)
     *      <-- deleteJAsync(T)
     *      <-- deleteJAsync(JsonObject)
     *      <-- deleteJAsync(JsonObject, pojo)
     *
     * delete(List<T>)
     *      <-- delete(JsonArray)
     *      <-- delete(JsonArray, pojo)
     *      <-- deleteJ(List<T>)
     *      <-- deleteJ(JsonArray)
     *      <-- deleteJ(JsonArray, pojo)
     *
     * deleteAsync(List<T>)
     *      <-- deleteAsync(JsonArray)
     *      <-- deleteAsync(JsonArray, pojo)
     *      <-- deleteJAsync(List<T>)
     *      <-- deleteJAsync(JsonArray)
     *      <-- deleteJAsync(JsonArray, pojo)
     *
     * deleteById(ID...)
     * deleteById(Collection<ID> ids)
     * deleteByIdAsync(ID...)
     * deleteByIdAsync(Collection<ID> ids)
     *
     * deleteBy(JsonObject)
     * deleteBy(JsonObject, pojo)
     * deleteByAsync(JsonObject)
     * deleteByAsync(JsonObject, pojo)
     */

    /*
     * deleteById
     * deleteByIdAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteById*(..)) && args(id)", argNames = "id")
    public <T> T deleteById(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Object[] / Collection
         */
        final List<CMessage> messages = this.messageList(id);
        return this.deleteAsync(messages, point);
    }

    /*
     * deleteBy
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteBy(..))")
    public Boolean deleteBy(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * Get all ids
             */
            final Object idSet = this.argumentCond(point);
            final List<CMessage> messages = this.messageList(idSet);
            return this.deleteAsync(messages, point);
        } else {
            /*
             * Pojo mode ignored
             */
            return (Boolean) point.proceed(point.getArgs());
        }
    }

    /*
     * deleteByAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteByAsync(..))")
    public Future<Boolean> deleteByAsync(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * Get all ids
             */
            final Object idSet = this.argumentCond(point);
            final List<CMessage> messages = this.messageList(idSet);
            return this.deleteAsync(messages, point);
        } else {
            /*
             * Pojo mode ignored
             */
            return (Future<Boolean>) point.proceed(point.getArgs());
        }
    }

    /*
     * delete(T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.delete(T))")
    public <T> T delete(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T / List<T>
         */
        final Object idSet = this.argumentT(point);
        final List<CMessage> messages = this.messageList(idSet);
        return this.deleteAsync(messages, point);
    }

    /*
     * delete(T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.delete(java.util.List))")
    public <T> List<T> deleteList(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T / List<T>
         */
        final Object idSet = this.argumentT(point);
        final List<CMessage> messages = this.messageList(idSet);
        return this.deleteAsync(messages, point);
    }

    /*
     * deleteAsync(T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteAsync(T))")
    public <T> Future<T> deleteAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T / List<T>
         */
        final Object idSet = this.argumentT(point);
        final List<CMessage> messages = this.messageList(idSet);
        return this.deleteAsync(messages, point);
    }

    /*
     * deleteAsync(List<T>)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteAsync(java.util.List))")
    public <T> Future<List<T>> deleteListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T / List<T>
         */
        final Object idSet = this.argumentT(point);
        final List<CMessage> messages = this.messageList(idSet);
        return this.deleteAsync(messages, point);
    }
}
