package io.vertx.tp.optic;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.atom.KEnv;
import io.vertx.tp.optic.environment.ES;
import io.vertx.tp.optic.environment.UnityAmbient;
import io.vertx.tp.optic.environment.UnityApp;
import io.vertx.up.eon.Values;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AtomHighway implements ES {
    private static final ConcurrentMap<String, KEnv> CACHE_BY_KEY = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, KEnv> CACHE_BY_SIGMA = new ConcurrentHashMap<>();

    @Override
    public KEnv connect(final String id) {
        this.initialize();
        KEnv env = CACHE_BY_SIGMA.getOrDefault(id, null);
        if (Objects.isNull(env)) {
            env = CACHE_BY_KEY.getOrDefault(id, null);
        }
        return env;
    }

    @Override
    public KEnv connect() {
        this.initialize();
        KEnv env = null;
        if (Values.ONE == CACHE_BY_KEY.size()) {
            env = CACHE_BY_KEY.values().iterator().next();
        }
        if (Objects.isNull(env) && Values.ONE == CACHE_BY_SIGMA.size()) {
            env = CACHE_BY_SIGMA.values().iterator().next();
        }
        return env;
    }

    private void initialize() {
        if (CACHE_BY_KEY.isEmpty()) {
            final UnityApp app = new UnityAmbient();
            final ConcurrentMap<String, JsonObject> appMap = app.connect();
            At.infoApp(this.getClass(), "[KEnv] Environment connecting..., size = {0}", String.valueOf(appMap.size()));
            appMap.forEach((appId, json) -> {
                final KEnv env = KEnv.instance(json);
                // Double `key` reference one KEnv reference
                CACHE_BY_KEY.put(appId, env);
                CACHE_BY_SIGMA.put(env.sigma(), env);
            });
        }
    }
}
