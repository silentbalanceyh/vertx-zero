package io.horizon.util;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class _Reflect extends _Random {
    protected _Reflect() {
    }

    /**
     * 执行SPI级的实例化操作，Service Provider Interface 的底层执行
     *
     * @param interfaceCls 接口类型
     * @param <T>          实例化的最终类型
     *
     * @return 实例化对象
     */
    public static <T> T service(final Class<T> interfaceCls) {
        return HSPI.service(interfaceCls);
    }

    /**
     * 执行SPI级的实例化操作，Service Provider Interface 的底层执行
     * 可传入类加载器执行模块级的SPI实例化操作
     *
     * @param interfaceCls 接口类型
     * @param loader       类加载器
     * @param <T>          实例化的最终类型
     *
     * @return 实例化对象
     */
    public static <T> T service(final Class<T> interfaceCls, final ClassLoader loader) {
        return HSPI.service(interfaceCls, loader);
    }

    /**
     * 判断 implCls 是否实现了 interfaceCls 接口，或者是否是 interfaceCls 的子类
     * 若 implCls 不是父类，则递归检索父类直到最终的 Object 类
     *
     * @param implCls      实现类
     * @param interfaceCls 接口类
     *
     * @return 是否实现
     */
    public static boolean isImplement(final Class<?> implCls, final Class<?> interfaceCls) {
        return TType.isImplement(implCls, interfaceCls);
    }

    /**
     * 判断 value 是否是一个类
     *
     * @param value 值
     *
     * @return 是否是类
     */
    public static boolean isClass(final Object value) {
        return TType.isClass(value);
    }

    /**
     * 根据类名在系统中反射查找该类，构造 Class<?> 对象
     * 整个流程分两步
     * 1. 如果找到的该类是 null，则使用默认的 instanceCls 返回
     * 2. 如果找到该类则使用其操作。
     * 为了让类加载器实现模块级操作，可传入额外的类加载器，优先从该类加载器中查找
     * <pre><code>
     * - loader
     * - Thread.currentThread().getContextClassLoader()
     * - ClassLoader.getSystemClassLoader()
     * - ClassLoader.getPlatformClassLoader()
     * </code></pre>
     *
     * @param className   类名
     * @param instanceCls 默认的类
     * @param loader      类加载器
     *
     * @return Class<?> 对象
     */
    public static Class<?> clazzBy(final String className, final Class<?> instanceCls,
                                   final ClassLoader loader) {
        return HInstance.clazz(className, instanceCls, loader);
    }


    /**
     * 根据类名在系统中反射查找该类，构造 Class<?> 对象
     * 整个流程分两步
     * 1. 如果找到的该类是 null，则使用默认的 instanceCls 返回
     * 2. 如果找到该类则使用其操作。
     * 为了让类加载器实现模块级操作，可传入额外的类加载器，优先从该类加载器中查找
     * <pre><code>
     * - loader
     * - Thread.currentThread().getContextClassLoader()
     * - ClassLoader.getSystemClassLoader()
     * - ClassLoader.getPlatformClassLoader()
     * </code></pre>
     *
     * 改名的主要目的是旧系统和模块应用中的调用会有冲突
     *
     * @param className 类名
     * @param loader    类加载器
     *
     * @return Class<?> 对象
     */
    public static Class<?> clazzBy(final String className, final ClassLoader loader) {
        return HInstance.clazz(className, null, loader);
    }

    /**
     * 根据类名在系统中反射查找该类，构造 Class<?> 对象
     * 整个流程分两步
     * 1. 如果找到的该类是 null，则使用默认的 instanceCls 返回
     * 2. 如果找到该类则使用其操作。
     * 为了让类加载器实现模块级操作，可传入额外的类加载器，优先从该类加载器中查找
     * <pre><code>
     * - loader
     * - Thread.currentThread().getContextClassLoader()
     * - ClassLoader.getSystemClassLoader()
     * - ClassLoader.getPlatformClassLoader()
     * </code></pre>
     *
     * @param className   类名
     * @param instanceCls 默认的类
     *
     * @return Class<?> 对象
     */
    public static Class<?> clazz(final String className, final Class<?> instanceCls) {
        return HInstance.clazz(className, instanceCls, null);
    }

    /**
     * 根据类名在系统中反射查找该类，构造 Class<?> 对象
     * 整个流程分两步
     * 1. 如果找到的该类是 null，则使用默认的 instanceCls 返回
     * 2. 如果找到该类则使用其操作。
     * 为了让类加载器实现模块级操作，可传入额外的类加载器，优先从该类加载器中查找
     * <pre><code>
     * - loader
     * - Thread.currentThread().getContextClassLoader()
     * - ClassLoader.getSystemClassLoader()
     * - ClassLoader.getPlatformClassLoader()
     * </code></pre>
     *
     * @param className 类名
     *
     * @return Class<?> 对象
     */
    public static Class<?> clazz(final String className) {
        return HInstance.clazz(className, null, null);
    }

    /**
     * 根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T
     *
     * @param clazz  类名
     * @param params 构造参数
     * @param <T>    T
     *
     * @return T
     */
    public static <T> T instance(final Class<?> clazz, final Object... params) {
        return HInstance.instance(clazz, params);
    }

    /**
     * 根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T
     *
     * @param className 类名
     * @param params    构造参数
     * @param <T>       T
     *
     * @return T
     */
    public static <T> T instance(final String className, final Object... params) {
        return HInstance.instance(clazz(className), params);
    }

    /**
     * （单例）根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T
     *
     * @param className 类名
     * @param params    构造参数
     * @param <T>       T
     *
     * @return T
     */
    public static <T> T singleton(final String className, final Object... params) {
        return HInstance.singleton(clazz(className), params);
    }

    /**
     * （单例）根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T
     *
     * @param clazz  类名
     * @param params 构造参数
     * @param <T>    T
     *
     * @return T
     */
    public static <T> T singleton(final Class<?> clazz, final Object... params) {
        return HInstance.singleton(clazz, params);
    }

    /**
     * （单例模式变种）根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T，外置传入构造逻辑
     *
     * @param clazz    类名
     * @param supplier 构造逻辑
     * @param <T>      T
     *
     * @return T
     */
    public static <T> T singleton(final Class<?> clazz, final Supplier<T> supplier) {
        return HInstance.singleton(clazz, (nil) -> supplier.get(), null);
    }

    /**
     * （单例模式变种）根据类名在系统中反射查找该类，并构造该类对应的实例，转换成T，外置传入构造逻辑
     * 此函数支持外置传入 key 构造池化的底层单件
     *
     * @param clazz    类名
     * @param supplier 构造逻辑
     * @param key      池化的维度专用键值
     * @param <T>      T
     *
     * @return T
     */
    public static <T> T singleton(final Class<?> clazz, final Supplier<T> supplier, final String key) {
        return HInstance.singleton(clazz, (nil) -> supplier.get(), key);
    }

    /**
     * 根据提供信息查找构造函数
     *
     * @param clazz  类
     * @param params 构造参数
     * @param <T>    T
     *
     * @return 构造函数
     */
    public static <T> Constructor<T> constructor(final Class<?> clazz, final Object... params) {
        return HInstance.constructor(clazz, params);
    }

    /**
     * 检查传入类是否带有默认无参构造函数
     *
     * @param clazz 类
     *
     * @return 是否带有默认无参构造函数
     */
    public static boolean isDefaultConstructor(final Class<?> clazz) {
        return HInstance.isDefaultConstructor(clazz);
    }
}
