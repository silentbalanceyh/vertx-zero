package io.vertx.up.annotations;

import io.vertx.up.eon.em.EmSecure;

import java.lang.annotation.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AuthorizedResource {
    EmSecure.AuthWord value() default EmSecure.AuthWord.AND;
}
