package io.vertx.tp.optic.environment;

import io.aeon.experiment.specification.power.KApp;
import io.aeon.runtime.H3H;
import io.vertx.aeon.specification.app.AbstractHET;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AmbientHighway extends AbstractHET {

    @Override
    public Future<Boolean> initialize() {
        // 统一接口：UnityAmbient，提供上层服务需初始化 H3H.CC_APP
        final UnityApp app = new UnityAmbient();
        // 读取初始化配置
        final ConcurrentMap<String, JsonObject> appMap = app.connect();
        At.infoHES(this.getClass(), "Environment connecting..., size = {0}", String.valueOf(appMap.size()));
        // 遍历每一个应用配置数据，填充 H3H.CC_APP
        appMap.forEach((appId, appJson) -> {
            // KApp 打印日志
            final KApp env = this.buildApp(appJson);
            At.infoHES(this.getClass(), "[HES] \tnamespace = {0}, name = {1}", env.ns(), env.name());
        });
        return Ux.futureT();
    }

    private KApp buildApp(final JsonObject unityApp) {
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
            /*
             * 1. 按名称构造
             * 2. 绑定附加数据
             * 3. 执行同步流程填充 H3H.CC_APP
             * -- name          = app
             * -- appKey        = app
             * -- appId         = app
             * -- code          = app
             * -- sigma         = app
             */
            () -> KApp.instance(name).bind(unityApp).synchro(),
            name
        );
    }
}
