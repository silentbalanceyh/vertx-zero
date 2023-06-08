package io.vertx.up.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Contract {
    /*
     * This annotation is used between different business component
     */
}
