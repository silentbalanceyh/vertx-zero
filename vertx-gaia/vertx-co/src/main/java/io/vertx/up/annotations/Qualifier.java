package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * Instead of javax.inject.Qualifier
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Qualifier {

    String value();
}
