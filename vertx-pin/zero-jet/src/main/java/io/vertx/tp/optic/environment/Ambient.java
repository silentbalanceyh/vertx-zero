package io.vertx.tp.optic.environment;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500AmbientConnectException;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.init.JtPin;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.up.eon.ID;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

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

    private static final Annal LOGGER = Annal.get(Ambient.class);

    public static Future<Boolean> init(final Vertx vertx) {
        try {
            /*
             * 1. UnityApp fetching here.
             */
            final UnityApp unity = JtPin.getUnity();
            Fn.out(null == unity, _500AmbientConnectException.class, Ambient.class);
            /*
             * 2. UnityApp initializing, the whole environment will be initianlized
             */
            return unity.initialize(vertx).compose(initialized -> {
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
                Jt.infoInit(LOGGER, "Ambient detect {0} applications in your environment.",
                    String.valueOf(APPS.keySet().size()));
                if (APPS.isEmpty()) {
                    Jt.warnInfo(LOGGER, "Ambient environment pool is Empty.");
                }
                final ConcurrentMap<String, Future<AmbientEnvironment>> futures = new ConcurrentHashMap<>();
                APPS.forEach((appId, app) -> futures.put(appId, new AmbientEnvironment(app).init(vertx)));
                return Fn.arrange(futures).compose(processed -> {
                    ENVIRONMENTS.putAll(processed);
                    Jt.infoInit(LOGGER, "AmbientEnvironment initialized !!!");
                    return Ux.future(Boolean.TRUE);
                });
            });
        } catch (Throwable ex) {
            ex.printStackTrace();
            return Future.failedFuture(ex);
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
                app = searchApp(sigma, JtApp::getSigma);
            }
            if (Objects.isNull(app)) {
                final String appKey = headers.get(ID.Header.X_APP_KEY);
                if (Ut.notNil(appKey)) {
                    app = searchApp(sigma, JtApp::getAppKey);
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
            Jt.warnApp(LOGGER, "Input key of app is null, key = {0}", key);
            Jt.warnApp(LOGGER, "You may missed the configuration of `io.vertx.tp.jet.init.JtPin` to on `init` node. ");
            return null;
        } else {
            JtApp app = APPS.get(key);
            if (Objects.isNull(app)) {
                /*
                 * sigma instead of appKey here
                 */
                app = searchApp(key, JtApp::getSigma);
            }
            /*
             * search app by key
             */
            return app;
        }
    }

    private static JtApp searchApp(final String key, final Function<JtApp, String> executor) {
        final JtApp app = APPS.values().stream()
            .filter(Objects::nonNull)
            .filter(appItem -> key.equals(executor.apply(appItem)))
            .findFirst().orElse(null);
        if (Objects.isNull(app)) {
            Jt.warnApp(LOGGER, "Ambient -> JtApp = null, input key = {0}", key);
        }
        return app;
    }
}
