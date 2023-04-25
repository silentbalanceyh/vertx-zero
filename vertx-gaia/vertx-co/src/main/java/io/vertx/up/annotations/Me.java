package io.vertx.up.annotations;

import io.horizon.eon.em.ValueBool;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Me {
    ValueBool active() default ValueBool.TRUE;

    boolean app() default false;
}
