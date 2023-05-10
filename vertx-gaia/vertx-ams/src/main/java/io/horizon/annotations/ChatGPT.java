package io.horizon.annotations;

import java.lang.annotation.*;

/**
 * @author lang : 2023-05-10
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ChatGPT {
}
