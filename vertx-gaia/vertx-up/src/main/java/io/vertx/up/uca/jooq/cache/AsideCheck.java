package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Aspect
@SuppressWarnings("all")
public class AsideCheck extends AbstractAside {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
    /*
     * All method of existing/missing
     *
     * existById(key)
     *      <-- missById(key)
     * existByIdAsync(key)
     *      <-- missByIdAsync(key)
     * exist(JsonObject)
     *      <-- miss(JsonObject)
     * exist(JsonObject, pojo)
     *      <-- miss(JsonObject, pojo)
     * existAsync(JsonObject)
     *      <-- missAsync(JsonObject)
     * existAsync(JsonObject, pojo)
     *      <-- missAsync(JsonObject, pojo)
     */

    /*
     * existById
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existById*(..))")
    public <T> T existById(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Returned Type checked only, two signatures
         */
        return null;
    }
}
