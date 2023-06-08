package io.vertx.up.runtime;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.exception.internal.EmptyIoException;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

class ZeroIo {
    public static final Cc<String, JsonObject> CC_STORAGE = Cc.open();

    private static String nameZero(final String key) {
        return Objects.isNull(key) ?
            "vertx" + VString.DOT + VPath.SUFFIX.YML :
            "vertx" + VString.DASH + key + VString.DOT + VPath.SUFFIX.YML;
    }

    /*
     * Zero标准容器读取专用
     * 1. extension = true：     会读取 /up/config/ 下的同名文件，二者执行合并
     * 2. extension = false：    只读取运行时中的相关文件
     */
    @SuppressWarnings("all")
    static JsonObject read(final String key, final boolean extension) {
        return read(key,
            // resources/vertx-xxx
            ZeroIo::nameZero,
            // vertx-co
            // resources/up/config/vertx-xxx
            extension ? name -> VPath.SERVER.INTERNAL_FILE + ZeroIo.nameZero(name) : null);
    }

    private static JsonObject read(final String fileSuffix,
                                   final Function<String, String> nameFn,
                                   final Function<String, String> nameInternalFn) {
        // Read the original configuration
        final JsonObject original = readDirect(nameFn.apply(fileSuffix));
        final JsonObject merged = new JsonObject();
        if (Objects.nonNull(nameInternalFn)) {
            // Read the internal configuration instead
            final JsonObject internal = readDirect(nameInternalFn.apply(fileSuffix));
            if (null != internal) {
                merged.mergeIn(internal, true);
            }
        }
        if (null != original) {
            merged.mergeIn(original, true);
        }
        return merged;
    }

    private static JsonObject readDirect(final String filename) {
        // Fix Docker issue
        final ConcurrentMap<String, JsonObject> dataRef = CC_STORAGE.store();
        if (dataRef.containsKey(filename)) {
            return dataRef.get(filename);
        } else {
            // Fix issue of deployment
            /*
             * For direct read configuration file, it must be successful or empty
             * Maybe the file is missing here
             */
            final JsonObject data = new JsonObject();
            try {
                final JsonObject yamlData = Ut.ioYaml(filename);
                if (Ut.isNotNil(yamlData)) {
                    data.mergeIn(yamlData);
                }
            } catch (final EmptyIoException ex) {
                // LOGGER.warn(ex.getMessage());
                // Here do nothing to avoid useless log out
                // ex.printStackTrace();
            }
            if (!data.isEmpty()) {
                dataRef.put(filename, data);
            }
            return data;
        }
    }
}
