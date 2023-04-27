package io.horizon.util;

import io.horizon.exception.internal.SPINullException;

import java.util.*;

/**
 * @author lang : 2023/4/27
 */
final class HSPI {
    private HSPI() {
    }

    static <T> Collection<T> services(final Class<T> clazz) {
        return services(clazz, null);
    }

    static <T> Collection<T> services(final Class<T> clazz, final ClassLoader classLoader) {
        final List<T> list = new ArrayList<>();
        ServiceLoader<T> factories;
        if (classLoader != null) {
            factories = ServiceLoader.load(clazz, classLoader);
        } else {
            // 等价代码：ServiceLoader.load(clazz, TCCL);
            factories = ServiceLoader.load(clazz);
        }
        if (factories.iterator().hasNext()) {
            factories.iterator().forEachRemaining(list::add);
            return list;
        } else {
            // 默认使用 TCCL，但在 OSGi 环境中可能不够，因此尝试使用加载此类的类加载器，所以为了兼容 osgi 环境，需要使用
            // ServiceLoader.load(clazz, Spi.class.getClassLoader()) 加载
            factories = ServiceLoader.load(clazz, HSPI.class.getClassLoader());
            if (factories.iterator().hasNext()) {
                factories.iterator().forEachRemaining(list::add);
                return list;
            } else {
                return Collections.emptyList();
            }
        }
    }

    static <T> T service(final Class<T> interfaceCls, final ClassLoader loader) {
        final Collection<T> collection = services(interfaceCls, loader);
        final T service;
        if (!collection.isEmpty()) {
            service = collection.iterator().next();
        } else {
            service = null;
        }
        if (Objects.isNull(service)) {
            throw new SPINullException(HSPI.class);
        }
        return service;
    }

    static <T> T service(final Class<T> interfaceCls) {
        return service(interfaceCls, null);
    }
}
