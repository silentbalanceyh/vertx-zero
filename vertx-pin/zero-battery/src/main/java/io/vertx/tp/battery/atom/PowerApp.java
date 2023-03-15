package io.vertx.tp.battery.atom;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.feature.Modulat;
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

    private static final ConcurrentMap<String, PowerApp> POWER_POOL = new ConcurrentHashMap<>();

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
            final JsonObject module = normalized.copy();
            module.remove(KName.KEY);
            Ut.<JsonObject>itJObject(module,
                (value, name) -> this.blocks.put(name, new PowerBlock(name, value)));
        }
    }

    public static Future<Boolean> flush(final String appId) {
        POWER_POOL.remove(appId);
        return initialize(appId).compose(nil -> Ux.futureT());
    }

    /*
     * static initialize by appId when delay extracing
     */
    public static Future<PowerApp> initialize(final String appId) {
        /*
         * Ke.channel
         */
        if (POWER_POOL.containsKey(appId)) {
            return Ux.future(POWER_POOL.getOrDefault(appId, null));
        } else {
            return Ux.channel(Modulat.class, JsonObject::new, modulat -> modulat.extension(appId)).compose(config -> {
                final PowerApp power = new PowerApp(appId, config);
                POWER_POOL.put(appId, power);
                return Ux.future(power);
            });
        }
    }


    public String appId() {
        return this.appId;
    }

    public PowerBlock block(final String name) {
        return this.blocks.getOrDefault(name, null);
    }
}
