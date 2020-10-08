package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Empty aspect for placeholder here
 */
// @Aspect
@SuppressWarnings("all")
public class AsideInsert extends AbstractAside {
    // @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
    /*
     * This is only the aspect that does not contain any point joint here
     *
     * insertAsync(T)
     *      <-- insertAsync(JsonObject)
     *      <-- insertAsync(JsonObject,pojo)
     *      <-- insertJAsync(T)
     *      <-- insertJAsync(JsonObject, pojo)
     *      <-- insertJAsync(JsonObject)
     *
     * insert(T)
     *      <-- insert(JsonObject)
     *      <-- insert(JsonObject, pojo)
     *      <-- insertJ(T)
     *      <-- insertJ(JsonObject)
     *      <-- insertJ(JsonObject, pojo)
     *
     * insertAsync(List<T>)
     *      <-- insertAsync(JsonArray)
     *      <-- insertAsync(JsonArray, pojo)
     *      <-- insertJAsync(List<T>)
     *      <-- insertJAsync(JsonArray)
     *      <-- insertJAsync(JsonArray, pojo)
     *
     * insert(List<T>)
     *      <-- insert(JsonArray)
     *      <-- insert(JsonArray,pojo)
     *      <-- insertJ(List<T>)
     *      <-- insertJ(JsonArray)
     *      <-- insertJ(JsonArray, pojo)
     *
     * In Aside-Cache mode, the INSERT could be very pure and it's not needed to set any
     * cache data here, in all FETCH, operations the cache will be created automatically
     */
}
