package io.vertx.up.secure.config;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AuthConfig implements Serializable {
    private static final String KEY = "secure";
    private static final Node<JsonObject> NODE = Ut.singleton(ZeroUniform.class);
    private static final ConcurrentMap<String, AuthConfig> SECURE = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(AuthConfig.class);

    static {
        final JsonObject config = NODE.read();
        final JsonObject configuration = Ut.sureJObject(config.getJsonObject(KEY));
        final Set<String> keys = AuthWall.keys();
        Ut.<JsonObject>itJObject(configuration, (value, field) -> {
            if (keys.contains(field)) {
                final String ruleKey = "wall-" + field;
                Fn.outUp(() -> Ruler.verify(ruleKey, value), LOGGER);
                SECURE.put(field, new AuthConfig(field, value));
            } else {
                LOGGER.warn("[ Auth ] The configuration key `{0}` could not be identified ({1}).",
                    field, Ut.fromJoin(keys));
            }
        });
        LOGGER.info("[ Auth ] You have configured `{0}` kind security mode.", String.valueOf(SECURE.size()));
    }

    private final transient JsonObject config = new JsonObject();
    private final transient String key;
    private final transient AuthWall wall;

    private AuthConfig(final String key, final JsonObject config) {
        this.key = key;
        this.wall = AuthWall.from(key);
        this.config.mergeIn(config, true);
    }

    public static ConcurrentMap<String, AuthConfig> configMap() {
        return SECURE;
    }

    public JsonObject config() {
        return this.config;
    }

    public AuthWall wall() {
        return this.wall;
    }

    public String key() {
        return this.key;
    }
}
