package io.vertx.up.annotations;

import io.vertx.up.eon.DefaultClass;
import io.vertx.up.eon.Strings;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Off {
    /*
     * Start job for the job input definition
     * - value: EventBus address
     * - outcome: income Implement class defined by `JobOutcome`
     */
    String address() default Strings.EMPTY;

    Class<?> outcome() default DefaultClass.class;
}
