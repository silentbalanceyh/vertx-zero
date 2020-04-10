package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * Only as comment for spec method.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Format {
}
