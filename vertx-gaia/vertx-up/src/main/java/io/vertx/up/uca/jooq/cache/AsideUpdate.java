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
public class AsideUpdate extends AbstractAside {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
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
}
