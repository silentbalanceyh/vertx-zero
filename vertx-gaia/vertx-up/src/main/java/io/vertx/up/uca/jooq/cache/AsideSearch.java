package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
// @Aspect
@SuppressWarnings("all")
public class AsideSearch extends AbstractAside {
    // @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
    /*
     * All method of searching operation
     *
     * searchAsync(JsonObject, pojo)
     * searchAsync(JsonObject)
     * search(JsonObject, pojo)
     * search(JsonObject)
     */

    /*
     * search is not support in current version
     */
}
