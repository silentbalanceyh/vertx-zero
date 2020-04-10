package io.vertx.up.annotations;

import io.vertx.up.eon.em.AuthPhase;

import java.lang.annotation.*;

/**
 * Authenticate phase for user defined
 * 1. HEADER: The phase to parse header and body
 * 2. AUTHORIZE: 401
 * 3. ACCESS: 403
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Phase {
    /**
     * Phase value
     *
     * @return Authentication phase for custom workflow
     */
    AuthPhase value();
}
