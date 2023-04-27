package io.horizon.specification.meta.typed;

/**
 * 对象拷贝包专用封装接口，不同于 clone() 方法是该接口支持泛型拷贝，您可提供自定义的拷贝实现
 * - 数据对象拷贝
 * - 配置对象拷贝
 * - 组件引用拷贝
 * - 缓存对象拷贝
 * 上述功能为目前规划的拷贝功能，可直接创建最新的配置副本。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TCopy<T> {
    T copy();
}
