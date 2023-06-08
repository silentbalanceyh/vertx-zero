package io.mature.extension.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

/**
 * ## 数据模拟工具
 *
 * ### 1. 基本介绍
 *
 * 数据模拟专用工具类，根据集成配置模拟数据内容。
 *
 * ### 2. 核心
 *
 * - 根据{@link Integration}中的`debug`配置决定执行器。
 * - 模拟数据三种类型`JsonObject, JsonArray, String`等。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxMocker {
    /*
     * 私有构造函数（工具类转换）
     */
    private OxMocker() {
    }

    /**
     * {@link JsonObject}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonObject} 返回最终数据记录
     */
    static JsonObject mockJ(final Integration integration, final String file, final Supplier<JsonObject> supplier) {
        return mock(integration, () -> {
            final JsonObject mockData = Ut.ioJObject(file);
            return Ut.valueJObject(mockData);
        }, supplier);
    }

    /**
     * {@link JsonArray}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonArray} 返回最终数据记录
     */
    static JsonArray mockA(final Integration integration, final String file, final Supplier<JsonArray> supplier) {
        return mock(integration, () -> {
            final JsonArray mockData = Ut.ioJArray(file);
            return Ut.valueJArray(mockData);
        }, supplier);
    }

    /**
     * {@link String}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link String} 返回最终数据记录
     */
    static String mockS(final Integration integration, final String file, final Supplier<String> supplier) {
        return mock(integration, () -> Ut.ioString(file)
            , supplier);
    }

    /**
     * 「Async」{@link JsonObject}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonObject} 返回最终数据记录
     */
    static Future<JsonObject> mockAsyncJ(final Integration integration, final String file, final Supplier<Future<JsonObject>> supplier) {
        return mock(integration, () -> {
            final JsonObject mockData = Ut.ioJObject(file);
            final JsonObject normalized = Ut.valueJObject(mockData);
            return Ux.future(normalized);
        }, supplier);
    }

    /**
     * 「Async」{@link JsonArray}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonArray} 返回最终数据记录
     */
    static Future<JsonArray> mockAsyncA(final Integration integration, final String file, final Supplier<Future<JsonArray>> supplier) {
        return mock(integration, () -> {
            final JsonArray mockData = Ut.ioJArray(file);
            final JsonArray normalized = Ut.valueJArray(mockData);
            return Ux.future(normalized);
        }, supplier);
    }

    /**
     * 「Async」{@link String}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link String} 返回最终数据记录
     */
    static Future<String> mockAsyncS(final Integration integration, final String file, final Supplier<Future<String>> supplier) {
        return mock(integration, () -> {
            final String normalized = Ut.ioString(file);
            return Ux.future(normalized);
        }, supplier);
    }

    /**
     * 内部数据模拟执行工具，读取{@link Integration}中的`debug`值，默认为false（真实环境）。
     *
     * @param integration    {@link Integration} 集成配置对象
     * @param mockSupplier   {@link Supplier} 模拟数据执行器
     * @param actualSupplier {@link Supplier} 真实执行器
     * @param <T>            真实执行器中构造的数据类型
     *
     * @return 返回最终执行结果数据（二选一环境）
     */
    private static <T> T mock(final Integration integration, final Supplier<T> mockSupplier, final Supplier<T> actualSupplier) {
        final boolean isDebug = integration.getOption(KName.DEBUG, Boolean.FALSE);
        if (isDebug) {
            return mockSupplier.get();
        } else {
            return actualSupplier.get();
        }
    }
}
