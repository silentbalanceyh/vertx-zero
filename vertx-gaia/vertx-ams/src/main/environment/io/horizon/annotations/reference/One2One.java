package io.horizon.annotations.reference;

import java.lang.annotation.*;

/**
 * 用于描述 1:1 关系专用，此注解直接使用在方法 Method 中用来描述 1:1 的关系，整体关系分四种：
 * <pre><code>
 *     1. One To One：直接使用当前注解进行描述
 *     2. One To Many / Many To One：一对多和多对一的关系，不需要描述，看返回值即可知道
 *     3. Many To Many：多对多的关系，也不需要描述，看返回值即可
 * </code></pre>
 * 类名中间使用 2 的目的是防止和 Hibernate/JPA 中的 @OneToOne 冲突
 *
 * @author lang : 2023-05-20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface One2One {
    /**
     * 1:1 关系的注解信息，您可以加入注解说明以辅助设计的可读性
     *
     * @return 注解说明
     */
    String value() default "";

    boolean interaction() default false;
}
