package io.vertx.up.uca.jooq.aop;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.JqAnalyzer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Aspect
@SuppressWarnings("all")
public class AsideOut {
    private static final Annal LOGGER = Annal.get(AsideOut.class);
    private transient L1Aside executor;

    @Before(value = "initialization(io.vertx.up.uca.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        this.executor = new L1Aside(JqAnalyzer.create(dao));
    }

    /*
     * Distinguish upsert situation
     * 1) upsert(String, T)
     * 2) upsert(JsonObject, T) / upsert(JsonObject, List<T>)
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.upsert*(..))")
    public <T> T upsert(final ProceedingJoinPoint point) throws Throwable {
        final Object[] args = point.getArgs();
        if (2 == args.length) {
            final Object firstArg = args[0];
            if (firstArg instanceof JsonObject) {
                final Object secondArg = args[1];
                if (secondArg instanceof List) {
                    /*
                     * upsert(JsonObject, List<T>)
                     */
                    LOGGER.info("( Aop ) upsert(JsonObject,List<T>) aspect execution.. {0}", args);
                    return null;
                } else {
                    /*
                     * upsert(JsonObject, T)
                     */
                    LOGGER.info("( Aop ) upsert(JsonObject,T) aspect execution.. {0}", args);
                    return null;
                }
            } else {
                /*
                 * upsert(String, T)
                 */
                LOGGER.info("( Aop ) upsert(String, T) aspect execution.. {0}", args);
                return null;
            }
        } else {
            /*
             * Skip aop directly
             */
            return (T) point.proceed(args);
        }
    }

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.delete(..))")
    public <T> T delete(final ProceedingJoinPoint point) throws Throwable {

        return null;
    }

    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteAsync(..))")
    public <T> T deleteAsync(final ProceedingJoinPoint point) throws Throwable {

        return null;
    }


    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.deleteById(..))")
    public <T> T deleteById(final ProceedingJoinPoint point) throws Throwable {
        final Object[] args = point.getArgs();

        return null;
    }

    /*
     * Distinguish update situation
     */
    @Around(value = "execution(* io.vertx.up.uca.jooq.UxJooq.update*(..))")
    public <T> T update(final ProceedingJoinPoint point) throws Throwable {
        final Object[] args = point.getArgs();
        if (1 == args.length) {
            final Object object = args[0];
            if (object instanceof List) {
                /*
                 * update( List<T> )
                 */
                LOGGER.info("( Aop ) update(List<T>) aspect execution.. {0}", args);
                return null;
            } else {
                /*
                 * update( T )
                 */
                LOGGER.info("( Aop ) update(T) aspect execution.. {0}", args);
                return null;
            }
        } else if (2 == args.length) {
            /*
             * update( id, T )
             */
            LOGGER.info("( Aop ) update(id,T) aspect execution.. {0}", args);
            return null;
        } else {
            /*
             * Skip aop directly
             */
            return (T) point.proceed(args);
        }
    }
}
