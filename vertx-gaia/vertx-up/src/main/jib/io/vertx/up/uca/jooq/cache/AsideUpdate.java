package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.up.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Empty aspect for placeholder here
 */
@Aspect
@SuppressWarnings("all")
public class AsideUpdate extends L1AsideWriting {
/*    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }*/
    /*
     * update(T)
     *      <-- update(JsonObject)
     *      <-- update(JsonObject, pojo)
     *      <-- updateJ(T)
     *      <-- updateJ(JsonObject)
     *      <-- updateJ(JsonObject, pojo)
     *
     * updateAsync(T)
     *      <-- updateAsync(JsonObject)
     *      <-- updateAsync(JsonObject, pojo)
     *      <-- updateAsyncJ(T)
     *      <-- updateAsyncJ(JsonObject)
     *      <-- updateAsyncJ(JsonObject, pojo)
     *
     * update(List<T>)
     *      <-- update(JsonArray)
     *      <-- update(JsonArray, pojo)
     *      <-- updateJ(T)
     *      <-- updateJ(JsonArray)
     *      <-- updateJ(JsonArray, pojo)
     *
     * updateAsync(List<T>)
     *      <-- updateAsync(JsonArray)
     *      <-- updateAsync(JsonArray, pojo)
     *      <-- updateJAsync(List<T>)
     *      <-- updateJAsync(JsonArray)
     *      <-- updateJAsync(JsonArray, pojo)
     *
     * update(id, T)
     *      <-- update(id, JsonObject)
     *      <-- update(id, JsonObject, pojo)
     *      <-- updateJ(id, T)
     *      <-- updateJ(id, JsonObject)
     *      <-- updateJ(id, JsonObject, pojo)
     *
     * updateAsync(id, T)
     *      <-- updateAsync(id, JsonObject)
     *      <-- updateAsync(id, JsonObject, pojo)
     *      <-- updateJAsync(id, T)
     *      <-- updateJAsync(id, JsonObject)
     *      <-- updateJAsync(id, JsonObject, pojo)
     *
     * update(criteria, T)
     *      <-- update(criteria, JsonObject)
     *      <-- updateJ(criteria, T)
     *      <-- updateJ(criteria, JsonObject)
     *
     * update(criteria, T, pojo)
     *      <-- update(criteria, JsonObject, pojo)
     *      <-- updateJ(criteria, T, pojo)
     *      <-- updateJ(criteria, JsonObject, pojo)
     *
     * updateAsync(criteria, T)
     *      <-- updateAsync(criteria, JsonObject)
     *      <-- updateJAsync(criteria, T)
     *      <-- updateJAsync(criteria, JsonObject)
     *
     * updateAsync(criteria, T, pojo)
     *      <-- updateAsync(criteria, JsonObject, pojo)
     *      <-- updateJAsync(criteria, T, pojo)
     *      <-- updateJAsync(criteria, JsonObject, pojo)
     */

    /*
     * update(T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update(T))")
    public <T> T update(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.updateAsync(T))")
    public <T> Future<T> updateAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<T>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(List<T>)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update(java.util.List))")
    public <T> List<T> updateList(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  List<T>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(List<T>)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.updateAsync(java.util.List))")
    public <T> Future<List<T>> updateListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<List<T>>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(id, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update(Object, T))")
    public <T> T updateById(final ProceedingJoinPoint point) throws Throwable {
        /*
         * T
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(id, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.updateAsync(Object, T))")
    public <T> Future<T> updateByIdAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<T>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update(io.vertx.core.json.JsonObject, T))")
    public <T> T updateByCond(final ProceedingJoinPoint point) throws Throwable {
        /*
         * T
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.updateAsync(io.vertx.core.json.JsonObject, T))")
    public <T> Future<T> updateByCondAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<T>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(JsonObject, T, String)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update(io.vertx.core.json.JsonObject, T, String))")
    public <T> T updateByCondPojo(final ProceedingJoinPoint point) throws Throwable {
        /*
         * T
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(JsonObject, T, String)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.updateAsync(io.vertx.core.json.JsonObject, T, String))")
    public <T> Future<T> updateByCondPojoAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<T>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }
}
