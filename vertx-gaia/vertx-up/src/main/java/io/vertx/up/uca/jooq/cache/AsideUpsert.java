package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Empty aspect for placeholder here
 */
@Aspect
@SuppressWarnings("all")
public class AsideUpsert extends AbstractAside {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
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
     * update(criteria, T)
     *      <-- update(criteria, JsonObject)
     *      <-- updateJ(criteria, T)
     *      <-- updateJ(criteria, JsonObject)
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
}
