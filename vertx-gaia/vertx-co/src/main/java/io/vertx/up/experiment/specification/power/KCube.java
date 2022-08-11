package io.vertx.up.experiment.specification.power;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * Environment Bus of Underway to Store critical Application
 * - sigma
 * - appId
 * - appKey
 * - language
 * - namespace
 * - name
 * - code
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KCube implements Serializable {
    private final transient String appId;   // X-App-Id
    private final transient String appKey;  // X-App-Key
    private final transient String code;
    private final transient Database database;
    private transient KApp app;

    private KCube(final JsonObject data) {
        final JsonObject dataJ = Ut.valueJObject(data);
        /*
         * appId
         * appKey
         * code
         */
        this.appId = dataJ.getString(KName.APP_ID);
        this.appKey = dataJ.getString(KName.APP_KEY);
        this.code = dataJ.getString(KName.CODE);
        final String name = dataJ.getString(KName.NAME);
        /*
         * name / ns / language / sigma combined
         */
        // this.app = H3H.CC_APP.pick(() -> new KApp(name), name);
        // this.app.sigma(dataJ.getString(KName.SIGMA));
        // this.app.language(dataJ.getString(KName.LANGUAGE));

        this.database = new Database();
        this.database.fromJson(dataJ.getJsonObject(KName.SOURCE, new JsonObject()));
    }

    public static KCube instance(final JsonObject unityApp) {
        return new KCube(unityApp);
    }

    public String sigma() {
        return this.app.sigma();
    }

    public String appId() {
        return this.appId;
    }

    public String appKey() {
        return this.appKey;
    }

    public String language() {
        return this.app.language();
    }

    public String name() {
        return this.app.name();
    }

    public String code() {
        return this.code;
    }

    public Database database() {
        return this.database;
    }

    @Override
    public String toString() {
        return "KEnv{" +
            "app=" + this.app +
            ", appId='" + this.appId + '\'' +
            ", appKey='" + this.appKey + '\'' +
            ", code='" + this.code + '\'' +
            ", database=" + this.database +
            '}';
    }
}
