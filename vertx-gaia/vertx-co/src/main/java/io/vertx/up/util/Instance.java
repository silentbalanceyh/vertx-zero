package io.vertx.up.util;

import io.horizon.eon.VValue;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class Instance {
    /*
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Instance.class);

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
        }

        final String pluginClsName = options.getString(key);
        if (HaS.isNil(pluginClsName)) {
            /*
             * class name is "" or null
             */
            return null;
        }

        final Class<?> pluginCls = HaS.clazz(pluginClsName, null);
        if (Objects.isNull(pluginCls)) {
            /*
             * class could not be found.
             */
            return null;
        } else {
            if (HaS.isImplement(pluginCls, interfaceCls)) {
                return HaS.instance(pluginCls);
            } else {
                /*
                 * The class does not implement interface Cls
                 */
                return null;
            }
        }
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
}
