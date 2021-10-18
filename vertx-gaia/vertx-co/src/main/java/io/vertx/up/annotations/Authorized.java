package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * This annotation must be inner @Wall
 * It's for 403 Response
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Authorized {
}
