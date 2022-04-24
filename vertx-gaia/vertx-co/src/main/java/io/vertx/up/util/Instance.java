package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.zero.DuplicatedImplException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroPack;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.cache.Cd;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked"})
final class Instance {

    private static final Annal LOGGER = Annal.get(Instance.class);

    private static final Cc<String, Object> CC_SINGLETON = Cc.open();
    private static final Cc<String, Object> CC_SERVICE_LOADER = Cc.open();

    private static final Cc<String, Class<?>> CC_CLASSES = Cc.open();

    private Instance() {
    }

    static <T> T service(final Class<T> interfaceCls) {
        if (Objects.isNull(interfaceCls) || !interfaceCls.isInterface()) {
            return null;
        } else {
            final Cd<String, Object> cData = CC_SERVICE_LOADER.data();
            return (T) CC_SERVICE_LOADER.pick(() -> {
                Object reference = cData.data(interfaceCls.getName());
                if (Objects.isNull(reference)) {
                    /*
                     * Service Loader for lookup input interface implementation
                     * This configuration must be configured in
                     * META-INF/services/<interfaceCls Name> file
                     */
                    final ServiceLoader<T> loader =
                        ServiceLoader.load(interfaceCls, interfaceCls.getClassLoader());
                    /*
                     * New data structure to put interface class into LEXEME_MAP
                     * In current version, it support one to one only
                     *
                     * 1) The key is interface class name
                     * 2) The found class is implementation name
                     */
                    for (final T t : loader) {
                        reference = t;
                        if (Objects.nonNull(reference)) {
                            cData.data(interfaceCls.getName(), reference);
                            break;
                        }
                    }
                }
                return reference;
            }, interfaceCls.getName());
        }
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
        final Object created = Fn.getJvm(
            () -> construct(clazz, params), clazz);
        return Fn.getJvm(() -> (T) created, created);
    }

    /**
     * Generic type
     */
    static Class<?> genericT(final Class<?> target) {
        return Fn.getJvm(() -> {
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
        return (T) CC_SINGLETON.pick(() -> instance(clazz, params), clazz);
        // Fn.po?l(SINGLETON, clazz.getName(), () -> instance(clazz, params));
    }

    static <T> T singleton(final Class<?> clazz, final Supplier<T> supplier) {
        return (T) CC_SINGLETON.pick(supplier::get, clazz);
        // Fn.po?l(SINGLETON, clazz.getName(), supplier::get);
    }

    /**
     * Get class from name, cached into memory pool
     *
     * @param name class Name
     *
     * @return Class<?>
     */
    static Class<?> clazz(final String name) {
        return CC_CLASSES.pick(() -> Fn.getJvm(() -> Thread.currentThread()
            .getContextClassLoader().loadClass(name), name), name);
        //        return Fn.po?l(Storage.CLASSES, name, () -> Fn.getJvm(() -> Thread.currentThread()
        //            .getContextClassLoader().loadClass(name), name));
    }

    static void clazzIf(final String name, final Consumer<Class<?>> consumer) {
        final Class<?> clazz = clazz(name, null);
        if (Objects.nonNull(clazz)) {
            consumer.accept(clazz);
        }
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
                final Cd<String, Class<?>> cData = CC_CLASSES.data();
                Class<?> clazz = cData.data(name);
                if (Objects.isNull(clazz)) {
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(name);
                    cData.data(name, clazz);
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
                LOGGER.error("[T] Error occurs in reflection, details: {0}", ex.getMessage());
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
        if (Types.isEmpty(options) || StringUtil.isNil(key)) {
            /*
             * options or key are either invalid
             */
            return null;
        } else {
            final String pluginClsName = options.getString(key);
            if (StringUtil.isNil(pluginClsName)) {
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
        return Fn.getNull(Boolean.FALSE, () -> {
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
        return Fn.getNull(null, () -> {
            final Set<Class<?>> classes = ZeroPack.getClasses();
            final List<Class<?>> filtered = classes.stream()
                .filter(item -> interfaceCls.isAssignableFrom(item)
                    && item != interfaceCls)
                .collect(Collectors.toList());
            final int size = filtered.size();
            // Non-Unique throw error out.
            Fn.outUp(Values.ONE < size, LOGGER,
                DuplicatedImplException.class,
                Instance.class, interfaceCls);
            // Null means direct interface only.
            return Values.ONE == size ? filtered.get(Values.IDX) : null;
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

    private static <T> T construct(final Class<?> clazz,
                                   final Object... params) {
        return Fn.getJvm(() -> {
            T ret = null;
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            // ZeroPack all constructors
            for (final Constructor<?> constructor : constructors) {
                // Fast to construct
                if (0 == params.length) {
                    ret = (T) construct(clazz);
                }
                // Fast to compare argument length
                if (params.length != constructor.getParameterTypes().length) {
                    continue;
                }
                // The slowest construct
                final Object reference = constructor.newInstance(params);
                ret = Fn.getJvm(() -> ((T) reference), reference);
            }
            return ret;
        }, clazz, params);
    }

    private static <T> T construct(final Class<T> clazz) {
        return Fn.getJvm(() -> {
            // Reflect Asm
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            final Constructor<?> constructor = Arrays.stream(constructors)
                .filter(item -> 0 == item.getParameterCount())
                .findAny().orElse(null);
            final T reference;
            if (Objects.nonNull(constructor)) {
                reference = (T) constructor.newInstance();
            } else {
                reference = null;
            }
            return reference;
            // final ConstructorAccess<T> access = ConstructorAccess.get(clazz);
            // return access.newInstance();
        }, clazz);
    }
}
