package io.vertx.up.commune.secure;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AegisItem implements Serializable {
    private static final ConcurrentMap<String, AegisItem> SECURE = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(AegisItem.class);

    static {
        //    final JsonObject configuration = Ut.valueJObject(config.getJsonObject(YmlCore.inject.SECURE));
        final JsonObject configuration = ZeroStore.option(YmlCore.inject.SECURE);
        final Set<String> keys = EmSecure.AuthWall.keys();
        Ut.<JsonObject>itJObject(configuration, (value, field) -> {
            if (keys.contains(field)) {
                final String ruleKey = "wall-" + field;
                Fn.outBug(() -> Ruler.verify(ruleKey, value), LOGGER);
                SECURE.put(field, new AegisItem(field, value));
            } else {
                LOGGER.info("[ Auth ] You have defined extension configuration with key `{0}`", field);
                SECURE.put(field, new AegisItem(EmSecure.AuthWall.EXTENSION.key(), value));
            }
        });
        LOGGER.info("[ Auth ] You have configured `{0}` kind security mode.", String.valueOf(SECURE.size()));
    }

    private final JsonObject options = new JsonObject();
    private final String key;
    private final EmSecure.AuthWall wall;
    private Class<?> providerAuthenticate;
    private Class<?> providerAuthorization;

    private AegisItem(final String key, final JsonObject config) {
        this.key = key;
        this.wall = EmSecure.AuthWall.from(key);
        /*
         * options:
         */
        this.options.mergeIn(config.getJsonObject(YmlCore.secure.OPTIONS, new JsonObject()), true);
        /*
         * provider class for current item
         * provider:
         *   authenticate:
         *   authorization:
         */
        this.init(config.getJsonObject(YmlCore.secure.PROVIDER, new JsonObject()));
    }

    public static ConcurrentMap<String, AegisItem> configMap() {
        return SECURE;
    }

    public static AegisItem configMap(final EmSecure.AuthWall wall) {
        return SECURE.getOrDefault(wall.key(), null);
    }

    private void init(final JsonObject provider) {
        // 401
        final String authenticate = provider.getString(YmlCore.secure.provider.AUTHENTICATE);
        if (Ut.isNil(authenticate)) {
            this.providerAuthenticate = null;
        } else {
            this.providerAuthenticate = Ut.clazz(authenticate);
        }
        // 403
        final String authorization = provider.getString(YmlCore.secure.provider.AUTHORIZATION);
        if (Ut.isNil(authorization)) {
            this.providerAuthorization = null;
        } else {
            this.providerAuthorization = Ut.clazz(authorization, null);
        }
    }

    public JsonObject options() {
        return this.options;
    }

    public EmSecure.AuthWall wall() {
        return this.wall;
    }

    public String key() {
        return this.key;
    }

    public Class<?> getProviderAuthenticate() {
        return this.providerAuthenticate;
    }

    public Class<?> getProviderAuthorization() {
        return this.providerAuthorization;
    }
}
