package io.horizon.util;

import io.horizon.exception.internal.OperationException;
import io.horizon.uca.cache.Cc;
import io.horizon.util.fn.HFn;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/28
 */
class HInstance {


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

    static Class<?> clazz(final String name) {
        return null;
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
                    .findAny().orElseThrow(() -> new OperationException(HInstance.class, "Constructor / 0 / 1", clazz));
                constructor.setAccessible(Boolean.TRUE);
                return HFn.failNOr(() -> ((T) constructor.newInstance(params)), constructor);
            } else {
                // 大于 1 深度构造
                final Class<?>[] types = types(params);
                try {
                    final Constructor<?> constructor = clazz.getDeclaredConstructor(types);
                    return HFn.failNOr(() -> ((T) constructor.newInstance(params)), constructor);
                } catch (final NoSuchMethodException ex) {
                    final Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(item -> length == item.getParameterTypes().length)
                        .filter(item -> typeMatch(item.getParameterTypes(), types))
                        .findAny().orElseThrow(() -> new OperationException(HInstance.class, "Constructor / N", clazz));
                    constructor.setAccessible(Boolean.TRUE);
                    return HFn.failNOr(() -> ((T) constructor.newInstance(params)), constructor);
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
}
