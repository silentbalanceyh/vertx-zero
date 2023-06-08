package io.modello.emf.specification;

import io.horizon.eon.VString;
import io.modello.specification.element.HType;

/**
 * 抽象类中已包含的方法
 * <pre><code>
 *     1. Java语言部分
 *        type(): 返回Java类型，反射专用，该类型必须包含无参构造函数，可直接通过反射构造
 *        typeName()：和 type() 同态，返回Java类型的字符串格式
 *     2. EMF对接部分
 *        eType(): 返回EMF中的 EClassifier 以及它的子类相关类型，主用于描述 EDataType 和 EClass
 *        eTypeName()：抽象层，不引用 emf 相关 jar 文件专用的字符串格式
 *     3. XML对接部分
 *        name(): 返回XML定义中的名称
 *        uri(): 返回XML定义中引用的名空间
 *        prefix()：返回XML定义中使用的名空间前缀
 * </code></pre>
 *
 * @author lang : 2023-05-13
 */
public interface EType<T> extends HType {
    /**
     * 返回 EMF 专用类型，此处T在子类限定，以防止在抽象类中引入 emf 相关的 jar 文件
     * 主要包含三种：
     * <pre><code>
     *    1. EDataType
     *    2. EClass
     *    3. EEnum
     * </code></pre>
     *
     * @return EMF 专用类型
     */
    T eType();

    /**
     * EMF 类型名称返回
     *
     * @return EMF 类型
     */
    default String eTypeName() {
        return VString.EMPTY;
    }
}
