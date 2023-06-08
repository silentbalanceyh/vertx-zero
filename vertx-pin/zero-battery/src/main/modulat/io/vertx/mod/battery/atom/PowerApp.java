package io.vertx.mod.battery.atom;

import io.horizon.annotations.Memory;
import io.horizon.spi.modeler.Modulat;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PowerApp implements Serializable {

    /*
     * 「应用模块集」
     * 用于存储 BBag + BBlock 等应用模块配置集
     */
    @Memory(Future.class)
    public static final Cc<String, Future<JsonArray>> CCA_BAG_DATA = Cc.openA();
    @Memory(Future.class)
    public static final Cc<String, Future<JsonObject>> CCA_BAG_ADMIN = Cc.openA();

    @Memory(Future.class)
    private static final Cc<String, Future<PowerApp>> CCA_APP_POWER = Cc.openA();

    private final transient String appId;
    private final transient ConcurrentMap<String, PowerBlock> blocks = new ConcurrentHashMap<>();

    /*
     * MetaPower for modulat
     */
    private PowerApp(final String appId, final JsonObject storedJson) {
        final JsonObject normalized = Ut.valueJObject(storedJson).copy();
        this.appId = appId;
        // key compared
        final String key = normalized.getString(KName.KEY);
        if (appId.equals(key)) {
            /*
             * 此处由于后期加入了 `bags` 导致下边遍历会失败，bags 不在 PowerApp 中执行
             * 所以要将 bags 移除，同时移除 `key`
             */
            normalized.remove(KName.KEY);
            normalized.remove(KName.App.BAGS);
            Ut.<JsonObject>itJObject(normalized,
                (value, name) -> this.blocks.put(name, new PowerBlock(name, value)));
        }
    }

    public static Future<Boolean> flush(final String appId) {
        CCA_APP_POWER.remove(appId);
        return initialize(appId).compose(nil -> Ux.futureT());
    }

    /*
     * static initialize by appId when delay extracing
     */
    public static Future<PowerApp> initialize(final String appId) {
        /*
         * Ke.channel
         */
        return CCA_APP_POWER.pick(() -> Ux.channel(Modulat.class, JsonObject::new,
            modulat -> modulat.extension(appId)).compose(config -> {
                final PowerApp power = new PowerApp(appId, config);
                return Ux.future(power);
            }
        ), appId);
    }


    public String appId() {
        return this.appId;
    }

    public PowerBlock block(final String name) {
        return this.blocks.getOrDefault(name, null);
    }
}
