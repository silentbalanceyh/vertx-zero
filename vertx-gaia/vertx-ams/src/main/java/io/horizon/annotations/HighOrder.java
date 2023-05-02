package io.horizon.annotations;

import java.lang.annotation.*;

/**
 * 无实际用途，仅作标注用于告知框架哪些内容是底层 Agreed Metadata Service 部分
 *
 * @author lang : 2023/4/27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HighOrder {
    Class<?> value();
}
