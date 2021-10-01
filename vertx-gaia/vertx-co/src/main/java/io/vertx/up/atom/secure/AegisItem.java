package io.vertx.up.atom.secure;

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
public class AegisItem implements Serializable {
    private static final String KEY = "secure";
    private static final Node<JsonObject> NODE = Ut.singleton(ZeroUniform.class);
    private static final ConcurrentMap<String, AegisItem> SECURE = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(AegisItem.class);

    static {
        final JsonObject config = NODE.read();
        final JsonObject configuration = Ut.sureJObject(config.getJsonObject(KEY));
        final Set<String> keys = AuthWall.keys();
        Ut.<JsonObject>itJObject(configuration, (value, field) -> {
            if (keys.contains(field)) {
                final String ruleKey = "wall-" + field;
                Fn.outUp(() -> Ruler.verify(ruleKey, value), LOGGER);
                SECURE.put(field, new AegisItem(field, value));
            } else {
                LOGGER.info("[ Auth ] You have defined extension configuration with key `{0}`", field);
                SECURE.put(field, new AegisItem(AuthWall.EXTENSION.key(), value));
            }
        });
        LOGGER.info("[ Auth ] You have configured `{0}` kind security mode.", String.valueOf(SECURE.size()));
    }

    private final transient JsonObject config = new JsonObject();
    private final transient String key;
    private final transient AuthWall wall;

    private AegisItem(final String key, final JsonObject config) {
        this.key = key;
        this.wall = AuthWall.from(key);
        this.config.mergeIn(config, true);
    }

    public static ConcurrentMap<String, AegisItem> configMap() {
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
