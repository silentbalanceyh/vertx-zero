package io.vertx.up.util;

import io.horizon.eon.VString;
import io.horizon.util.HH;
import io.vertx.up.atom.Kv;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroSerializer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Env {

    static boolean readBool(final String name) {
        final String ioDebug = System.getenv(name);
        return "true".equalsIgnoreCase(ioDebug);
    }


    static String readEnv(final String name, final String defaultValue) {
        final String parsed = System.getenv(name);
        return Ut.isNil(parsed) ? defaultValue : parsed;
    }

    static <T> T readEnv(final String name, final Class<T> clazz) {
        return readEnv(name, null, clazz);
    }

    /*
     * 可直接将字符串的 literal 根据Zero内置序列化子系统反序列化成
     * 想要的数据类型对应的值，主要针对 boolean, int, double 等
     */
    static <T> T readEnv(final String name, final T defaultValue, final Class<T> clazz) {
        final String literal = readEnv(name, VString.EMPTY);
        if (Ut.isNil(literal)) {
            return defaultValue;
        }

        final T value = ZeroSerializer.getValueT(clazz, literal);
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        return value;
    }

    /*
     * JDK 9 之后要带上启动参数才允许
     * --add-opens java.base/java.util=ALL-UNNAMED
     * --add-opens java.base/java.lang=ALL-UNNAMED
     */
    @SuppressWarnings("unchecked")
    static ConcurrentMap<String, String> writeEnv(final Properties properties) {
        // 提取环境变量引用
        final Map<String, String> envMap = envMap();

        final ConcurrentMap<String, String> envResult = new ConcurrentHashMap<>();
        final Enumeration<String> it = (Enumeration<String>) properties.propertyNames();
        while (it.hasMoreElements()) {
            final String name = it.nextElement();
            final String value = properties.getProperty(name);
            // .env.development （环境变量设置，JDK 11之后带参数执行）
            if (HH.isNotNil(value)) {
                envMap.put(name, value);
                envResult.put(name, value);
            }
        }
        return envResult;
    }

    static Kv<String, String> writeEnv(final String name, final String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        final Map<String, String> envMap = envMap();
        envMap.put(name, value);
        return Kv.create(name, value);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> envMap() {
        return Fn.orJvm(() -> {
            final Map<String, String> env = System.getenv();
            final Field field = env.getClass().getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> mValue = (Map<String, String>) field.get(env);
            // Fix issue: Cannot invoke "java.util.Map.put(Object,Object)" because "envMap" is null
            if (Objects.isNull(mValue)) {
                mValue = new HashMap<>();
            }
            return mValue;
        });
    }
}
