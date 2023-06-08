package io.horizon.specification.typed;

/**
 * 「对象拷贝器」
 * <hr/>
 * 对象拷贝包专用封装接口，不同于 clone() 方法是该接口支持泛型拷贝，您可提供自定义的拷贝实现
 * <pre><code>
 *      - 数据对象拷贝
 *      - 配置对象拷贝
 *      - 组件引用拷贝
 *      - 缓存对象拷贝
 * </code></pre>
 * 上述功能为目前规划的拷贝功能，可直接创建最新的配置副本，和原始版本不同的点在于原始版本的
 * JVM级别的 clone 拷贝出来的类型是 {@link Object} 类型，而这里的拷贝是泛型类型，可以直接
 * 拷贝一个 T 或者 T 类型的子类，最终可以实现不使用 () 操作符做类型强转的方式拷贝对象，这种
 * 做法的核心目的在于屏蔽底层实现细节，以接口拷贝为主的模式完成对象的拷贝。
 * <pre><code>
 *     1. 以 interface 为中心的拷贝模式
 *     2. 抽象拷贝
 *     3. 泛型拷贝
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TCopy<T> {
    /**
     * 拷贝当前对象，返回新的对象实例，泛型 T 限定了拷贝的类型的最终根类型
     *
     * @param <CHILD> 子类类型
     *
     * @return 拷贝后的新对象
     */
    <CHILD extends T> CHILD copy();
}
