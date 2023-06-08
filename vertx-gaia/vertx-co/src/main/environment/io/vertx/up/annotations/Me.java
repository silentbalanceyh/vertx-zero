package io.vertx.up.annotations;

import io.modello.eon.em.EmValue;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Me {
    EmValue.Bool active() default EmValue.Bool.TRUE;

    boolean app() default false;
}
