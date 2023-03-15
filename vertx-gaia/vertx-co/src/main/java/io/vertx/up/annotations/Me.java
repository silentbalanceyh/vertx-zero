package io.vertx.up.annotations;

import io.vertx.up.eon.em.BoolStatus;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Me {
    BoolStatus active() default BoolStatus.TRUE;

    boolean app() default false;
}
