package io.vertx.up.util;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.exception.internal.OperationException;
import io.horizon.uca.cache.Cc;
import io.horizon.util.HaS;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InvokeErrorException;
import io.vertx.up.exception.zero.DuplicatedImplException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked"})
final class Instance {
    /*
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Instance.class);

    private static final Cc<String, Object> CC_SINGLETON = Cc.open();

    private static final Cc<String, Class<?>> CC_CLASSES = Cc.open();

    /*
     * 快速构造对象专用内存结构
     * Map ->  clazz = constructor length / true|false
     *         true  = 构造函数无长度型重载，只有一个方法
     *         false = 构造函数出现了重载
     */
    private static final ConcurrentMap<Class<?>, ConcurrentMap<Integer, Integer>> BUILD_IN =
        new ConcurrentHashMap<>();

    private Instance() {
    }

    /**
     * Create new instance with reflection
     *
     * @param clazz  the instance type that you want to create
     * @param params constructor parameters of this type
     * @param <T>    Returned instance type
     *
     * @return return new created instance
     */

    static <T> T instance(final Class<?> clazz,
                          final Object... params) {
        final Object created = Fn.failOr(
            () -> construct(clazz, params), clazz);
        return Fn.failOr(() -> (T) created, created);
    }

    static WebException errorWeb(final Throwable ex) {
        if (ex instanceof WebException) {
            return (WebException) ex;
        } else {
            final Throwable target = ex.getCause();
            if (Objects.isNull(target)) {
                return new _500InvokeErrorException(Instance.class, ex);
            } else {
                return errorWeb(target);
            }
        }
    }

    /**
     * Generic type
     */
    static Class<?> genericT(final Class<?> target) {
        return Fn.failOr(() -> {
            final Type type = target.getGenericSuperclass();
            return (Class<?>) (((ParameterizedType) type).getActualTypeArguments()[0]);
        }, target);
    }

    /**
     * Singleton instances
     */
    static <T> T singleton(final Class<?> clazz,
                           final Object... params) {
        // Must reference to created first.
        return (T) CC_SINGLETON.pick(() -> instance(clazz, params), clazz.getName());
        // Fn.po?l(SINGLETON, clazz.getName(), () -> instance(clazz, params));
    }

    static <T> T singleton(final Class<?> clazz, final Supplier<T> supplier) {
        return singleton(clazz, supplier, null);
        // Fn.po?l(SINGLETON, clazz.getName(), supplier::get);
    }

    static <T> T singleton(final Class<?> clazz, final Supplier<T> supplier, final String extensionKey) {
        if (Ut.isNil(extensionKey)) {
            return (T) CC_SINGLETON.pick(supplier::get, clazz.getName());
        } else {
            return (T) CC_SINGLETON.pick(supplier::get, clazz.getName() + VString.SLASH + extensionKey);
        }
    }

    /**
     * Get class from name, cached into memory pool
     *
     * @param name class Name
     *
     * @return Class<?>
     */
    static Class<?> clazz(final String name) {
        return CC_CLASSES.pick(() -> Fn.failOr(() -> Thread.currentThread()
            .getContextClassLoader().loadClass(name), name), name);
        //        return Fn.po?l(Storage.CLASSES, name, () -> Fn.getJvm(() -> Thread.currentThread()
        //            .getContextClassLoader().loadClass(name), name));
    }

    static Class<?> clazz(final String name, final Class<?> defaultCls) {
        if (Ut.isNil(name)) {
            return defaultCls;
        } else {
            try {
                /*
                 * Here must capture 'ClassNotFound` issue instead of
                 * Throw exception out for null reference returned.
                 * Specific situation usage.
                 *
                 * Here we could not call `clazz(name)` because of
                 * getJvm will throw out exception here. in current method, we should
                 * catch `ClassNotFoundException` and return null directly.
                 */
                final ConcurrentMap<String, Class<?>> cData = CC_CLASSES.store();
                Class<?> clazz = cData.get(name);
                if (Objects.isNull(clazz)) {
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(name);
                    cData.put(name, clazz);
                }
                /*
                 * Result checking
                 */
                if (Objects.isNull(clazz)) {
                    return defaultCls;
                } else {
                    return clazz;
                }
            } catch (final Throwable ex) {
                LOGGER.error("[T] Error occurs in reflection, details: {}", ex.getMessage());
                return defaultCls;
            }
        }
    }

    /*
     * Enhancement for interface plugin initialized
     * 1) Get the string from `options[key]`
     * 2) Initialize the `key` string ( class name ) with interfaceCls
     */
    static <T> T plugin(final JsonObject options, final String key, final Class<?> interfaceCls) {
        if (HaS.isNil(options) || HaS.isNil(key)) {
            /*
             * options or key are either invalid
             */
            return null;
        } else {
            final String pluginClsName = options.getString(key);
            if (HaS.isNil(pluginClsName)) {
                /*
                 * class name is "" or null
                 */
                return null;
            } else {
                final Class<?> pluginCls = clazz(pluginClsName, null);
                if (Objects.isNull(pluginCls)) {
                    /*
                     * class could not be found.
                     */
                    return null;
                } else {
                    if (isMatch(pluginCls, interfaceCls)) {
                        return instance(pluginCls);
                    } else {
                        /*
                         * The class does not implement interface Cls
                         */
                        return null;
                    }
                }
            }
        }
    }

    /**
     * Check whether clazz implement the interfaceCls
     *
     * @param clazz        classname/interface name
     * @param interfaceCls interface name that will be implement
     *
     * @return whether OK here
     */
    @SuppressWarnings("all")
    static boolean isMatch(final Class<?> clazz, final Class<?> interfaceCls) {
        final Class<?>[] interfaces = clazz.getInterfaces();
        boolean match = Arrays.stream(interfaces)
            .anyMatch(item -> item.equals(interfaceCls));
        if (!match) {
            /* continue to check parent */
            if (Objects.nonNull(clazz.getSuperclass())) {
                match = isMatch(clazz.getSuperclass(), interfaceCls);
            }
        }
        return match;
    }

    /**
     * Whether the class contains no-arg constructor
     */
    static boolean noarg(final Class<?> clazz) {
        return Fn.runOr(Boolean.FALSE, () -> {
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            boolean noarg = false;
            for (final Constructor<?> constructor : constructors) {
                if (0 == constructor.getParameterTypes().length) {
                    noarg = true;
                    break;
                }
            }
            return noarg;
        }, clazz);
    }

    /**
     * Find the unique implementation for interfaceCls
     */
    static Class<?> child(final Class<?> interfaceCls) {
        return Fn.runOr(null, () -> {
            final Set<Class<?>> classes = ZeroPack.getClasses();
            final List<Class<?>> filtered = classes.stream()
                .filter(item -> interfaceCls.isAssignableFrom(item)
                    && item != interfaceCls)
                .toList();
            final int size = filtered.size();
            // Non-Unique throw error out.
            if (VValue.ONE < size) {
                final UpException error = new DuplicatedImplException(Instance.class, interfaceCls);
                LOGGER.error("[T] Error occurs {}", error.getMessage());
                throw error;
            }
            // Null means direct interface only.
            return VValue.ONE == size ? filtered.get(VValue.IDX) : null;
        }, interfaceCls);
    }

    public static <T> Constructor<T> constructor(final Class<?> clazz,
                                                 final Object... params) {
        Constructor<T> result = null;
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (final Constructor<?> constructor : constructors) {
            if (params.length == constructor.getParameterTypes().length) {
                result = (Constructor<T>) constructor;
                break;
            }
        }
        return result;
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
    private static <T> T construct(final Class<?> clazz,
                                   final Object... params) {
        /*
         * 计算是否存在重载的函数
         */
        final int length = params.length;
        if (BUILD_IN.containsKey(clazz)) {
            final ConcurrentMap<Integer, Integer> map = BUILD_IN.get(clazz);
            final Integer counter = map.getOrDefault(length, 0);
            if (1 >= counter) {
                // 0, 1 直接构造
                final Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(item -> length == item.getParameterTypes().length)
                    .findAny().orElseThrow(() -> new OperationException(Instance.class, "Constructor / 0 / 1", clazz));
                constructor.setAccessible(Boolean.TRUE);
                return Fn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
            } else {
                // 大于 1 深度构造
                final Class<?>[] types = types(params);
                try {
                    final Constructor<?> constructor = clazz.getDeclaredConstructor(types);
                    return Fn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
                } catch (final NoSuchMethodException ex) {
                    final Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(item -> length == item.getParameterTypes().length)
                        .filter(item -> typeMatch(item.getParameterTypes(), types))
                        .findAny().orElseThrow(() -> new OperationException(Instance.class, "Constructor / N", clazz));
                    constructor.setAccessible(Boolean.TRUE);
                    return Fn.failOr(() -> ((T) constructor.newInstance(params)), constructor);
                }
            }
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

    private static class NULL {
    }
    //
    //    private static <T> T construct(final Class<?> clazz,
    //                                   final Object... params) {
    //        return Fn.orJvm(() -> {
    //            T ret = null;
    //            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    //            // ZeroPack all constructors
    //            for (final Constructor<?> constructor : constructors) {
    //                // Fast to construct
    //                if (0 == params.length) {
    //                    ret = (T) constructor.newInstance();
    //                }
    //                // Fast to compare argument length
    //                if (params.length != constructor.getParameterTypes().length) {
    //                    continue;
    //                }
    //                // The slowest construct
    //                final Object reference = constructor.newInstance(params);
    //                ret = Fn.orJvm(() -> ((T) reference), reference);
    //            }
    //            return ret;
    //        }, clazz, params);
    //    }
}
