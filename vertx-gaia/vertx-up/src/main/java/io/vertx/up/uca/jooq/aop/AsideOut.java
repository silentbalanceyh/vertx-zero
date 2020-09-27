package io.vertx.up.uca.jooq.aop;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.JqAnalyzer;
import org.aspectj.lang.ProceedingJoinPoint;
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

    public <T> T update(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Distinguish update situation
         * 1) update(T)
         * 2) update(id, T)
         * 3) update(List<T>)
         */
        final Object[] args = point.getArgs();
        if (1 == args.length) {
            final Object object = args[0];
            if (object instanceof List) {
                /*
                 * List<T>
                 */
                LOGGER.info("( Aop ) update(List<T>) aspect execution.. {0}", args);
                return null;
            } else {
                /*
                 * T
                 */
                LOGGER.info("( Aop ) update(T) aspect execution.. {0}", args);
                return null;
            }
        } else if (2 == args.length) {
            /*
             * id, T
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
