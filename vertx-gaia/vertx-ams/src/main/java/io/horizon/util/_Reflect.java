package io.horizon.util;

/**
 * @author lang : 2023/4/28
 */
class _Reflect extends _Parse {
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
}
