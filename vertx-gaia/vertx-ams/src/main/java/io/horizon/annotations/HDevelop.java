package io.horizon.annotations;

import java.lang.annotation.*;

/**
 * 无实际用途，仅作标注用于告知框架哪些内容是开发专用的部分方法
 *
 * @author lang : 2023/4/30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HDevelop {
    String value() default "";      // 注释说明
}
