package io.modello.annotations;

import java.lang.annotation.*;

/**
 * 统一托管 EMF 中使用的 Annotation 部分
 * EA 全称：EMF Annotation
 * 内部所有的Java注解对应到XML中的节点或属性部分
 *
 * @author lang : 2023-05-08
 */
public interface EA {

    /**
     * 接口专用注解，用于注解属性类接口，其中 value() 表示它注解的属性名称
     * 对应属性名称会牵涉到 attr1=, attr2=, ... 等核心结构处理，主要用于
     * 描述XML中的属性部分
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface Attribute {
        String value();
    }

    /**
     * EMF中对应的内容如
     *
     * <pre><code class="xml">
     *      <eLiterals attr1="value1"
     *                 attr2="value2"/>
     * </code></pre>
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface Node {
        String value();
    }

    /**
     * EMF中对应的属性片段
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface Segment {

    }
}
