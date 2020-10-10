package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Aspect
@SuppressWarnings("all")
public class AsideCheck extends L1AsideReading {
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
     * existByIdAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existById*(..)) && args(id)", argNames = "id")
    public <T> T existById(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Returned Type checked only
         */
        final CMessage message = this.message(id);
        return this.existAsync(message, point);
    }

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.exist*(..))")
    public <T> T exist(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * exist(JsonObject)
             * existAsync(JsonObject)
             */
            final CMessage message = this.messagesCond(point);
            return this.existAsync(message, point);
        } else {
            return (T) point.proceed(point.getArgs());
        }
    }
}
