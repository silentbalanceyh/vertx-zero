package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.bridge.FileSuffix;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class ZeroTool {
    static final Cc<String, JsonObject> CC_STORAGE = Cc.open();

    public static String nameZero(final String key) {
        return Objects.isNull(key) ?
            "vertx" + Strings.DOT + FileSuffix.YML :
            "vertx" + Strings.DASH + key + Strings.DOT + FileSuffix.YML;
    }

    public static String nameAeon(final String key, final boolean galaxy) {
        if (galaxy) {
            return Objects.isNull(key) ?
                // Fix Issue: aeon/xxx -> aeon/contained/xxx
                "zapp" + Strings.DOT + FileSuffix.YML :
                "zapp" + Strings.DASH + key + Strings.DOT + FileSuffix.YML;
        } else {
            return Objects.isNull(key) ?
                // Fix Issue: aeon/xxx -> aeon/contained/xxx
                "zcloud" + Strings.DOT + FileSuffix.YML :
                "zcloud" + Strings.DASH + key + Strings.DOT + FileSuffix.YML;
        }
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
            ZeroTool::nameZero,
            // vertx-co
            // resources/up/config/vertx-xxx
            extension ? name -> Values.CONFIG_INTERNAL_FILE + ZeroTool.nameZero(name) : null);
    }

    /*
     * Aeon系统读取配置专用
     * 1. extension = true：     一定会读取 /aeon/contained/ 下的同名文件
     * 2. 优先检查是否开启aeon系统：
     */
    static JsonObject readCloud(final String key, final boolean galaxy) {
        return read(key,
            // resources/aeon/zapp-xxx, zcloud-xxx
            name -> "aeon/" + nameAeon(name, galaxy),
            // resources/aeon/contained/zapp-xxx, zcloud-xxx
            name -> Values.CONFIG_INTERNAL_CLOUD + nameAeon(name, galaxy));
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
                if (Ut.notNil(yamlData)) {
                    data.mergeIn(yamlData);
                }
            } catch (final EmptyStreamException ex) {
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
