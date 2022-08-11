package io.vertx.aeon.specification.app;

import io.vertx.aeon.runtime.H3H;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「环境工厂」
 * 静态环境工厂专用方法，替换原始的 Highway 部分的连接，并且从接口中剥离出来，可直接调用
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HES {
    private static final Annal LOGGER = Annal.get(HES.class);

    /*
     * HAtom 建模上下文专用
     * 1. 内置调用 H3H 核心数据结构
     * 2. 按 name 提取（两种模式）,     context
     * 3. 按 sigma/appId 提取,        connect（原ES方法）
     */
    public static KApp context(final String name) {
        /*
         * 无填充模式，key = value，此时 key 就是应用程序名称
         */
        return H3H.CC_APP.pick(() -> KApp.instance(name), name);
    }

    public static KApp context(final JsonObject unityApp) {
        /*
         * 附加填充模式，key = value，此时 key 优先选择应用程序名称
         * 由于此时通常为动态建模初始化，所以 key 会执行附加填充，填充键
         * - appId
         * - appKey
         * - sigma
         * - code
         */
        final String name = Ut.valueString(unityApp, KName.NAME);
        return H3H.CC_APP.pick(
            () -> KApp.instance(name).bind(unityApp).synchro(),
            name
        );
    }

    public static KApp connect(final String id) {
        return H3H.CC_APP.store(id);
    }

    /*
     * 连接当前应用，工作流程
     * 1. 先调用 HET 的 initialize 方法（如果CC_APP为空则执行，如果不为空不执行初始化）
     * 2. 填充最终上下文环境提取当前应用，调用 values()，只有一个则返回
     */
    public static KApp connect() {
        final Set<KApp> appSet = new HashSet<>(H3H.CC_APP.store().values());
        if (appSet.isEmpty()) {
            appSet.addAll(initialize());
        }
        KApp env = null;
        if (Values.ONE == appSet.size()) {
            env = appSet.iterator().next();
        }
        return env;
    }

    private static Set<KApp> initialize() {
        return Ux.channelS(HET.class, het -> {
            // 执行初始化配置信息
            final ConcurrentMap<String, JsonObject> appMap = het.initialize();
            LOGGER.info("[KApp] Environment connecting..., size = {0}", String.valueOf(appMap.size()));
            final Set<KApp> appSet = new HashSet<>();
            appMap.forEach((appId, json) -> {
                final KApp env = context(json);
                LOGGER.info("[KApp] \tnamespace = {0}, name = {1}", env.ns(), env.name());
                appSet.add(env);
            });
            return appSet;
        });
    }
}
