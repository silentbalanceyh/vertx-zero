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
public class AsideUpsert extends L1AsideWriting {
/*
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
*/

    /*
     * upsert(id, T)
     *      <-- upsert(id, JsonObject)
     *      <-- upsert(id, JsonObject, pojo)
     *      <-- upsertJ(id, T)
     *      <-- upsertJ(id, JsonObject)
     *      <-- upsertJ(id, JsonObject, pojo)
     *
     * upsertAsync(id, T)
     *      <-- upsertAsync(id, JsonObject)
     *      <-- upsertAsync(id, JsonObject, pojo)
     *      <-- upsertJAsync(id, T)
     *      <-- upsertJAsync(id, JsonObject)
     *      <-- upsertJAsync(id, JsonObject, pojo)
     *
     * upsert(criteria, T)
     *      <-- upsert(criteria, JsonObject)
     *      <-- upsertJ(criteria, T)
     *      <-- upsertJ(criteria, JsonObject)
     *
     * upsert(criteria, T, pojo)
     *      <-- upsert(criteria, JsonObject, pojo)
     *      <-- upsertJ(criteria, T, pojo)
     *      <-- upsertJ(criteria, JsonObject, pojo)
     *
     * upsertAsync(criteria, T)
     *      <-- upsertAsync(criteria, JsonObject)
     *      <-- upsertJAsync(criteria, T)
     *      <-- upsertJAsync(criteria, JsonObject)
     *
     * upsertAsync(criteria, T, pojo)
     *      <-- upsertAsync(criteria, JsonObject, pojo)
     *      <-- upsertJAsync(criteria, T, pojo)
     *      <-- upsertJAsync(criteria, JsonObject, pojo)
     *
     * upsert(criteria, list, finder)
     *      <-- upsert(criteria, JsonArray, finder)
     *      <-- upsertJ(criteria, list, finder)
     *      <-- upsertJ(criteria, JsonArray, finder)
     *
     * upsert(criteria, list, finder, pojo)
     *      <-- upsert(criteria, JsonArray, finder, pojo)
     *      <-- upsertJ(criteria, list, finder, pojo)
     *      <-- upsertJ(criteria, JsonArray, finder, pojo)
     *
     * upsertAsync(criteria, list, finder)
     *      <-- upsertAsync(criteria, JsonArray, finder)
     *      <-- upsertJAsync(criteria, list, finder)
     *      <-- upsertJAsync(criteria, JsonArray, finder)
     *
     * upsertAsync(criteria, list, finder, pojo)
     *      <-- upsertAsync(criteria, JsonArray, finder, pojo)
     *      <-- upsertJAsync(criteria, list, finder, pojo)
     *      <-- upsertJAsync(criteria, JsonArray, finder, pojo)
     */
    /*
     * upsert(id, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert(Object,T))")
    public <T> T upsert(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  T
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(id, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsertAsync(Object,T))")
    public <T> Future<T> upsertAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<T>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert(io.vertx.core.json.JsonObject, T))")
    public <T> T upsertByCond(final ProceedingJoinPoint point) throws Throwable {
        /*
         * T
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, T))")
    public <T> Future<T> upsertByCondAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<T>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert(io.vertx.core.json.JsonObject, T, String))")
    public <T> T upsertByPojo(final ProceedingJoinPoint point) throws Throwable {
        /*
         * T
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, T)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, T, String))")
    public <T> Future<T> upsertByPojoAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<T>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, T, BiPredicate)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate))")
    public <T> List<T> upsertList(final ProceedingJoinPoint point) throws Throwable {
        /*
         * List<T>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, T, BiPredicate)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate))")
    public <T> Future<List<T>> upsertListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<List<T>>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }


    /*
     * upsert(JsonObject, T, BiPredicate, String)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate, String))")
    public <T> List<T> upsertListFn(final ProceedingJoinPoint point) throws Throwable {
        /*
         * List<T>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, T, BiPredicate,  String)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate, String))")
    public <T> Future<List<T>> upsertListFnAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<List<T>>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }
}
