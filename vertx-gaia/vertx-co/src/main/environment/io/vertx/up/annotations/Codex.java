package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * Enabled rule validation for current api
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Codex {
}
