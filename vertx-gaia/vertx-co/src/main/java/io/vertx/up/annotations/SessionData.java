package io.vertx.up.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SessionData {
    String value();

    /**
     * If the return type is JsonObject, you can extract one field to store into session
     *
     * @return stored key in session
     */
    String field() default "";
}
