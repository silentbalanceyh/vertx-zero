package io.vertx.up.annotations;

import io.vertx.up.eon.em.AuthWord;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AuthorizedResource {
    AuthWord value() default AuthWord.AND;
}
