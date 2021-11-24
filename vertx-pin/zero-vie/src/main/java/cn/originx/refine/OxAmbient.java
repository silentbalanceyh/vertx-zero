package cn.originx.refine;

import cn.originx.cv.OxCv;
import cn.vertxup.ambient.service.application.AppService;
import cn.vertxup.ambient.service.application.AppStub;
import cn.vertxup.ambient.service.application.InitService;
import cn.vertxup.ambient.service.application.InitStub;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ## 环境工具
 *
 * ### 1. 基本介绍
 *
 * Ox平台系统中定义了三种核心环境：
 *
 * - Development：开发环境。
 * - Production：生产环境。
 * - Mockito：模拟测试环境（集成debug=true）。
 *
 * ### 2. 工具介绍：
 *
 * 当前工具类为包域，它的执行功能如：
 *
 * 1. 从`UQUIP`中提取字典翻译器。
 * 2. 构造专用初始化执行器。
 * 3. 辅助执行通道专用的插件组件、服务配置构造。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxAmbient {
    /*
     * 私有构造函数（工具类转换）
     */
    private OxAmbient() {
    }

    /**
     * 从原始配置数据中读取`plugin.config`节点，根据传入的`clazz`读取和当前插件相关的配置信息。
     *
     * - 从原始配置中读取基础配置，上层传入的`options`数据。
     * - 提取`plugin.config`中和`clazz`绑定的配置数据。
     * - 合并二者所有配置数据构造最终配置数据。
     *
     * @param clazz   {@link Class} 执行初始化的类信息，反射调用
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     *
     * @return {@link JsonObject}
     */
    static JsonObject pluginOptions(final Class<?> clazz, final JsonObject options) {
        final JsonObject normalized = Ut.sureJObject(options);
        final JsonObject configData = Ut.sureJObject(normalized.getJsonObject(OxCv.PLUGIN_CONFIG)).copy();
        /*
         * 查找单个组件配置
         */
        final JsonObject configuration = new JsonObject();
        if (configData.containsKey(clazz.getName())) {
            final JsonObject componentConfig = configData.getJsonObject(clazz.getName());
            configuration.mergeIn(componentConfig, true);
        }
        /*
         * 将 plugin 开头的过滤掉
         */
        normalized.stream()
            .filter(entry -> Objects.nonNull(entry.getKey()))
            .filter(entry -> !entry.getKey().startsWith("plugin"))
            .forEach(entry -> configuration.put(entry.getKey(), entry.getValue()));
        return configuration;
    }

    /**
     * 返回插件链上的所有组件类信息，可执行反射操作。
     *
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     * @param key     {@link String} 传入的固定`key`值
     *
     * @return {@link List}<{@link Class}>
     */
    static List<Class<?>> pluginClass(final JsonObject options, final String key) {
        final JsonObject normalized = Ut.sureJObject(options);
        final JsonArray configured = Ut.sureJArray(normalized.getJsonArray(key));
        final List<Class<?>> queue = new ArrayList<>();
        Ut.itJArray(configured, String.class, (className, index) -> {
            final Class<?> clazz = Ut.clazz(className, null);
            final int current = index + 1;
            if (Objects.nonNull(clazz)) {
                Ox.Log.infoPlugin(OxAmbient.class, "{0}. 插件类: {1}", current, className);
                queue.add(clazz);
            } else {
                Ox.Log.infoPlugin(OxAmbient.class, "{0}. 插件类异常（null）: {1}", current, className);
            }
        });
        return queue;
    }

    /**
     * 根据传入路径执行路径解析工作，解析三种不同环境的路径。
     *
     * - Development：开发环境
     * - Production：生产环境
     * - Mockito：模拟测试环境（integration中的debug = true）
     *
     * @param path        {@link String} 构造的环境读取数据专用路径
     * @param environment {@link Environment} 传入环境数据
     *
     * @return {@link String} 返回不同环境处理过的绝对路径信息
     */
    static String resolve(final String path, final Environment environment) {
        if (Environment.Production == environment) {
            return path;
        } else {
            return "src/main/resources/" + path;
        }
    }

    /**
     * 构造初始化专用接口，用于初始化某个`X_APP`的应用配置。
     *
     * - 初始化接口{@link InitStub}，执行应用初始化。
     * - 应用访问接口{@link AppStub}。
     *
     * @return {@link InitStub}
     */
    static InitStub pluginInitializer() {
        /*
         * 初始化专用（同样需要初始化注入字段）
         */
        final InitStub stub = Ut.singleton(InitService.class);
        final AppStub appStub = Ut.singleton(AppService.class);
        Ut.field(stub, "stub", appStub);
        return stub;
    }
}
