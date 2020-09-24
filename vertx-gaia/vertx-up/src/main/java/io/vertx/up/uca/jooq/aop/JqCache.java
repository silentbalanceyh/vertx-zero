package io.vertx.up.uca.jooq.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Aspect
public class JqCache {

    @Pointcut(value = "execution(* io.vertx.up.uca.jooq.Ux*.findById(Object)) && args(input)", argNames = "input")
    public void ajFindById(final Object input) {
    }

    @Around(value = "ajFindById(input)", argNames = "input")
    public Object ajProcessedById(final ProceedingJoinPoint point,
                                  final Object input) throws Throwable {
        System.out.println("Hello");
        return point.proceed();
    }
}
