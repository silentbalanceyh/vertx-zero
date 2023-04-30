package io.horizon.annotations;

import java.lang.annotation.*;

/**
 * @author lang : 2023/4/30
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Memory {
    Class<?> value();      // 缓存的类
}
