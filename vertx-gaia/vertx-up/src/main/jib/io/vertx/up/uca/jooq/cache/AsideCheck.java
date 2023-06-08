package io.vertx.up.uca.jooq.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Aspect
@SuppressWarnings("all")
public class AsideCheck extends L1AsideReading {
    /*    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }*/
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
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existById(..)) && args(id)", argNames = "id")
    public Boolean existById(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Returned Type checked only
         */
        final CMessage message = this.messageKey(id, point);
        return this.existSync(message, point);
    }

    /*
     * existByIdAsync
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existByIdAsync(..)) && args(id)", argNames = "id")
    public Future<Boolean> existByIdAsync(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Returned Type checked only
         */
        final CMessage message = this.messageKey(id, point);
        return this.existAsync(message, point);
    }

    /*
     * exist
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.exist(..))")
    public Boolean exist(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * exist(JsonObject)
             */
            final CMessage message = this.messageListCond(point);
            return this.existSync(message, point);
        } else {
            /*
             * exist(JsonObject, pojo)
             */
            final CMessage message = this.messageListPojo(point);
            return this.existSync(message, point);
        }
    }

    /*
     * existAsync
     */

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.existAsync(..))")
    public Future<Boolean> existAsync(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * existAsync(JsonObject)
             */
            final CMessage message = this.messageListCond(point);
            return this.existAsync(message, point);
        } else {
            /*
             * existAsync(JsonObject, pojo)
             */
            final CMessage message = this.messageListPojo(point);
            return this.existAsync(message, point);
        }
    }
}
