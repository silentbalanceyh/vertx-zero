package io.horizon.spi.cloud;

import io.aeon.runtime.CRunning;
import io.horizon.atom.app.KApp;
import io.horizon.spi.environment.UnityAmbient;
import io.horizon.spi.environment.UnityApp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentMap;

import static io.vertx.tp.ambient.refine.At.LOG;

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
        LOG.HES.info(this.getClass(), "Environment connecting..., size = {0}", String.valueOf(appMap.size()));
        // 遍历每一个应用配置数据，填充 H3H.CC_APP
        appMap.forEach((appId, appJson) -> {
            // KApp 打印日志
            final KApp env = this.buildApp(appJson);
            LOG.HES.info(this.getClass(), "[HES] \tnamespace = {0}, name = {1}", env.ns(), env.name());
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
        return CRunning.CC_APP.pick(
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
