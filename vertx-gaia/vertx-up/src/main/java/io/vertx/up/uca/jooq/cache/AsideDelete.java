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
public class AsideDelete extends AbstractAside {
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
}
