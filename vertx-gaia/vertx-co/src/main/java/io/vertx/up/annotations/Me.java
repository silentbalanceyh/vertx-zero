package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Me {
    boolean active() default true;

    boolean app() default false;
}
