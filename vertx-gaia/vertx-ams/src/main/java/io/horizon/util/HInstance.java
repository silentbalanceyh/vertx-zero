package io.horizon.util;

import io.horizon.annotations.Memory;
import io.horizon.eon.VString;
import io.horizon.exception.internal.OperationException;
import io.horizon.fn.HFn;
import io.horizon.uca.cache.Cc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface CACHE {
    /**
     * 全局单件模式专用
     */
    @Memory(Object.class)
    Cc<String, Object> CC_SINGLETON = Cc.open();
    /**
     * 全局类缓存专用
     */
    @Memory(Class.class)
    Cc<String, Class<?>> CC_CLASSES = Cc.open();
}

/**
 * @author lang : 2023/4/28
 */
@SuppressWarnings("all")
class HInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(HInstance.class);
    /*
     * 快速构造对象专用内存结构
     * Map ->  clazz = constructor length / true|false
     *         true  = 构造函数无长度型重载，只有一个方法
     *         false = 构造函数出现了重载
     */
    private static final ConcurrentMap<Class<?>, ConcurrentMap<Integer, Integer>> BUILD_IN =
        new ConcurrentHashMap<>();

    static boolean isDefaultConstructor(final Class<?> clazz) {
        return HFn.runOr(Boolean.FALSE, () -> {
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            return Arrays.stream(constructors)
                .anyMatch(constructor -> 0 == constructor.getParameterTypes().length);
        }, clazz);
    }

    @SuppressWarnings("all")
    static Object instanceArray(final Object array, final Class<?> newArrayComponentType) {
        if (array != null) {
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    static <T> T instance(final Class<?> clazz, final Object... params) {
        // 截断构造，如果clazz为null则不构造
        if (Objects.isNull(clazz)) {
            return null;
        }
        return HFn.failOr(() -> construct(clazz, params), clazz);
    }

    static <T> T singleton(final Class<?> clazz, final Object... params) {
        return (T) CACHE.CC_SINGLETON.pick(() -> instance(clazz, params), clazz.getName());
    }

    static <T> T singleton(final Class<?> clazz, final Function<Class<?>, T> instanceFn, final String key) {
        if (TIs.isNil(key)) {
            /*
             * 如果key为空，则直接返回，等价于原始 singleton
             * 并且其构造函数的参数是无参的，只是切换了 instanceFn 的实现
             */
            return (T) CACHE.CC_SINGLETON.pick(() -> instanceFn.apply(clazz), clazz.getName());
        } else {
            /*
             * 池化，键值：className/key
             */
            return (T) CACHE.CC_SINGLETON.pick(() -> instanceFn.apply(clazz), clazz.getName() + VString.SLASH + key);
        }
    }

    static Class<?> clazz(final String name, final Class<?> instanceCls, final ClassLoader loader) {
        if (TIs.isNil(name)) {
            return instanceCls;
        } else {

            /*
             * Here must capture 'ClassNotFound` issue instead of
             * Throw exception out for null reference returned.
             * Specific situation usage.
             *
             * Here we could not call `clazz(name)` because of
             * getJvm will throw out exception here. in current method, we should
             * catch `ClassNotFoundException` and return null directly.
             */
            final ConcurrentMap<String, Class<?>> cData = CACHE.CC_CLASSES.store();
            Class<?> clazz = cData.get(name);
            if (Objects.isNull(clazz)) {
                /* 优先从传入的类加载器中加载 */
                try {
                    if (Objects.nonNull(loader)) {
                        clazz = loader.loadClass(name);
                    }
                } catch (final Throwable ex) {
                    LOGGER.error("[T] (Module) Error occurs in reflection, details: {}",
                        ex.getMessage());
                }
                /* 再从当前线程中加载 */
                if (Objects.isNull(clazz)) {
                    try {
                        clazz = Thread.currentThread().getContextClassLoader().loadClass(name);
                    } catch (final Throwable ex) {
                        LOGGER.error("[T] (Program) Error occurs in reflection, details: {}",
                            ex.getMessage());
                    }
                }
                /* 最后从当前类加载器中加载 */
                if (Objects.isNull(clazz)) {
                    try {
                        clazz = ClassLoader.getSystemClassLoader().loadClass(name);
                    } catch (final Throwable ex) {
                        LOGGER.error("[T] (System) Error occurs in reflection, details: {}",
                            ex.getMessage());
                    }
                }
                /* 平台类加载器 */
                if (Objects.isNull(clazz)) {
                    try {
                        clazz = ClassLoader.getPlatformClassLoader().loadClass(name);
                    } catch (final Throwable ex) {
                        LOGGER.error("[T] (Platform) Error occurs in reflection, details: {}",
                            ex.getMessage());
                    }
                }
                if (Objects.isNull(clazz)) {
                    clazz = instanceCls;
                } else {
                    // 缓存填充
                    cData.put(name, clazz);
                }
            }
            return clazz;
        }
    }

    /*
     * 快速构造的基础算法
     * 1. 若能直接检索到唯一构造函数，直接调用构造方法（反射），跳过复杂检查
     * 2. 缓存得到某个 clazz 中的构造函数数量：
     *    clazz = length / counter
     * 3. 第二轮直接根据长度提取 counter，0 / 1 时都可快速构造
     * 4. 出现重载时（长度多个），则直接根据半群计算笛卡尔匹配级（上三角匹配）得到最终构造函数
     * 5. 解开 Accessible 提高构造效率并可访问私有
     */
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> constructor(final Class<?> clazz, final Object... params) {
        final int length = params.length;
        // Fix:Cannot invoke "java.util.concurrent.ConcurrentMap.getOrDefault(Object, Object)" because "map" is null
        final ConcurrentMap<Integer, Integer> map = BUILD_IN.getOrDefault(clazz, new ConcurrentHashMap<>());
        final Integer counter = map.getOrDefault(length, 0);
        Constructor<T> constructor;
        if (1 >= counter) {
            // 0, 1 直接构造
            constructor = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(item -> length == item.getParameterTypes().length)
                .map(item -> (Constructor<T>) item)
                .findAny().orElseThrow(() -> new OperationException(HInstance.class, "Constructor / 0 / 1", clazz));
            constructor.setAccessible(Boolean.TRUE);
            //            return HFn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
        } else {
            // 大于 1 深度构造
            final Class<?>[] types = types(params);
            try {
                constructor = (Constructor<T>) clazz.getDeclaredConstructor(types);
                constructor.setAccessible(Boolean.TRUE);
                //                return HFn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
            } catch (final NoSuchMethodException ex) {
                constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(item -> length == item.getParameterTypes().length)
                    .filter(item -> typeMatch(item.getParameterTypes(), types))
                    .map(item -> (Constructor<T>) item)
                    .findAny().orElseThrow(() -> new OperationException(HInstance.class, "Constructor / N", clazz));
                constructor.setAccessible(Boolean.TRUE);
                //                return HFn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
            } finally {
                constructor = null;
            }
        }
        return constructor;
    }

    private static <T> T construct(final Class<?> clazz,
                                   final Object... params) {
        /*
         * 计算是否存在重载的函数
         */
        if (BUILD_IN.containsKey(clazz)) {
            final Constructor<T> constructor = constructor(clazz, params);
            return HFn.failOr(() -> constructor.newInstance(params), constructor);
        } else {
            // 填充数据后递归
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            final ConcurrentMap<Integer, Integer> map = new ConcurrentHashMap<>();
            for (final Constructor<?> each : constructors) {
                Integer counter = map.getOrDefault(each.getParameterTypes().length, 0);
                counter++;
                map.putIfAbsent(each.getParameterTypes().length, counter);
            }
            BUILD_IN.put(clazz, map);
            return construct(clazz, params);
        }
    }

    private static Class<?>[] types(final Object... values) {
        if (values == null) {
            return new Class[0];
        }

        final Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            final Object value = values[i];
            result[i] = value == null ? NULL.class : value.getClass();
        }

        return result;
    }

    private static boolean typeMatch(final Class<?>[] declaredTypes, final Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == NULL.class) {
                    continue;
                }

                if (typeWrap(declaredTypes[i]).isAssignableFrom(typeWrap(actualTypes[i]))) {
                    continue;
                }

                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private static <T> Class<T> typeWrap(final Class<T> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return (Class<T>) Boolean.class;
            } else if (int.class == type) {
                return (Class<T>) Integer.class;
            } else if (long.class == type) {
                return (Class<T>) Long.class;
            } else if (short.class == type) {
                return (Class<T>) Short.class;
            } else if (byte.class == type) {
                return (Class<T>) Byte.class;
            } else if (double.class == type) {
                return (Class<T>) Double.class;
            } else if (float.class == type) {
                return (Class<T>) Float.class;
            } else if (char.class == type) {
                return (Class<T>) Character.class;
            } else if (void.class == type) {
                return (Class<T>) Void.class;
            }
        }
        return type;
    }

    static boolean isImplement(final Class<?> implCls, final Class<?> interfaceCls) {
        final Class<?>[] interfaces = implCls.getInterfaces();
        boolean match = Arrays.asList(interfaces).contains(interfaceCls);
        if (!match) {
            /* continue to check parent */
            if (Objects.nonNull(implCls.getSuperclass())) {
                match = isImplement(implCls.getSuperclass(), interfaceCls);
            }
        }
        return match;
    }

    private static class NULL {
    }
}
