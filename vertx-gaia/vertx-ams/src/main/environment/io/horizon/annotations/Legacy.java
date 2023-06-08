package io.horizon.annotations;

import java.lang.annotation.*;

/**
 * @author lang : 2023/5/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Legacy {
    String value() default "";      // 注释说明
}
