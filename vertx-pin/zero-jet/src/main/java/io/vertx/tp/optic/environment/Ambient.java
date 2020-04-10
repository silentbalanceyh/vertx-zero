package io.vertx.tp.optic.environment;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500AmbientConnectException;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.init.JtPin;
import io.vertx.up.eon.ID;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * The environment data, it's for multi-app deployment here
 * Here must be defined ambient for App / Source
 */
@SuppressWarnings("all")
public class Ambient {
    /* Each application has one environment */
    private static final ConcurrentMap<String, JtApp> APPS =
            new ConcurrentHashMap<>();

    /* XHeader information of Ambient */
    private static final ConcurrentMap<String, AmbientEnvironment> ENVIRONMENTS =
            new ConcurrentHashMap<>();

    static {
        try {
            /*
             * 1. UnityApp fetching here.
             */
            final UnityApp unity = JtPin.getUnity();
            Fn.out(null == unity, _500AmbientConnectException.class, Ambient.class);
            /*
             * 2. UnityApp initializing, the whole environment will be initianlized
             */
            unity.initialize();
            /*
             * 3. Application environment initialization
             */
            final ConcurrentMap<String, JsonObject> unityData = unity.connect();
            unityData.forEach((key, json) -> APPS.put(key, Ut.deserialize(json, JtApp.class)));
            /*
             * 4. Binding configuration of this environment
             * - DSLContext ( reference )
             * - Service/Api -> Uri
             * - Router -> Route of Vert.x
             */
            APPS.forEach((appId, app) -> ENVIRONMENTS.put(appId, new AmbientEnvironment(app).init()));
        } catch (Throwable ex) {
            // TODO: Start up exception
            ex.printStackTrace();
        }
    }

    public static ConcurrentMap<String, AmbientEnvironment> getEnvironments() {
        return ENVIRONMENTS;
    }

    public static JtApp getCurrent(final MultiMap headers) {
        /*
         * 1. First search X-App-Id
         */
        final String appId = headers.get(ID.Header.X_APP_ID);
        if (Ut.notNil(appId)) {
            return APPS.get(appId);
        } else {
            /*
             * 2. Then lookup X-Sigma
             */
            final String sigma = headers.get(ID.Header.X_SIGMA);
            JtApp app = null;
            if (Ut.notNil(sigma)) {
                app = APPS.values().stream()
                        .filter(each -> sigma.equals(each.getSigma()))
                        .findFirst().orElse(null);
            }
            if (Objects.isNull(app)) {
                final String appKey = headers.get(ID.Header.X_APP_KEY);
                if (Ut.notNil(appKey)) {
                    app = APPS.values().stream()
                            .filter(each -> appKey.equals(each.getAppKey()))
                            .findFirst().orElse(null);
                }
            }
            return app;
        }
    }

    public static JtApp getApp(final String key) {
        /*
         * Search JtApp environment by
         * 1) key ( Recommend performance )
         * 2) sigma search ( Secondary priority )
         */
        if (Ut.isNil(key)) {
            return null;
        } else {
            final JtApp app = APPS.get(key);
            if (Objects.isNull(app)) {
                /*
                 * sigma instead of appKey here
                 */
                return APPS.values().stream()
                        .filter(Objects::nonNull)
                        .filter(appItem -> key.equals(appItem.getSigma()))
                        .findFirst().orElse(null);
            } else {
                /*
                 * search app by key
                 */
                return app;
            }
        }
    }
}
