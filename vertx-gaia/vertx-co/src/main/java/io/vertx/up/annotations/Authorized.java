package io.vertx.up.annotations;

import io.vertx.up.eon.em.AuthWord;

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
    /*
     * The default connect word is `AND`
     */
    AuthWord value() default AuthWord.AND;
}
