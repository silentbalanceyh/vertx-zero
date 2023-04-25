package io.vertx.up.annotations;

import io.horizon.eon.VString;
import io.vertx.up.eon.DefaultClass;

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
    String address() default VString.EMPTY;

    Class<?> outcome() default DefaultClass.class;
}
