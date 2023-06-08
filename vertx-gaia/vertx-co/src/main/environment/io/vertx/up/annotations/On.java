package io.vertx.up.annotations;

import io.horizon.eon.VString;
import io.vertx.up.eon.DefaultClass;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface On {
    /*
     * Start job for the job input definition
     * - value: EventBus address
     * - income: income Implement class defined by `JobIncome`
     */
    String address() default VString.EMPTY;

    Class<?> income() default DefaultClass.class;
}
