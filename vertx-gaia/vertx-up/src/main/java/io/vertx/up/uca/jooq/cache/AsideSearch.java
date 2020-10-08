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
public class AsideSearch extends AbstractAside {
    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
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
     * search
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.search*(..))")
    public <T> T search(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Returned Type checked only, two signatures
         */
        return null;
    }
}
