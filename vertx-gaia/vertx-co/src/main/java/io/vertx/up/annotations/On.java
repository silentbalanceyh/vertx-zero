package io.vertx.up.annotations;

import io.vertx.up.eon.DefaultClass;
import io.vertx.up.eon.Strings;

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
    String address() default Strings.EMPTY;

    Class<?> income() default DefaultClass.class;
}
