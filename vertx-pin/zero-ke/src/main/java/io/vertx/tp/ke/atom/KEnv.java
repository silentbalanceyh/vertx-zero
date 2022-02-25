package io.vertx.tp.ke.atom;

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
public class KEnv implements Serializable {
    private final transient String sigma;
    private final transient String appId;
    private final transient String appKey;
    private final transient String language;
    private final transient String name;
    private final transient String code;
    private final transient Database database;

    private KEnv(final JsonObject data) {
        final JsonObject dataJ = Ut.valueJObject(data);
        /*
         * appId
         * appKey
         * sigma
         */
        this.appId = dataJ.getString(KName.APP_ID);
        this.appKey = dataJ.getString(KName.APP_KEY);
        this.sigma = dataJ.getString(KName.SIGMA);
        /*
         * language
         * name / code
         */
        this.code = dataJ.getString(KName.CODE);
        this.name = dataJ.getString(KName.NAME);
        this.language = dataJ.getString(KName.LANGUAGE);

        this.database = new Database();
        this.database.fromJson(dataJ.getJsonObject(KName.SOURCE, new JsonObject()));
    }

    public static KEnv instance(final JsonObject unityApp) {
        return new KEnv(unityApp);
    }

    public String sigma() {
        return this.sigma;
    }

    public String appId() {
        return this.appId;
    }

    public String appKey() {
        return this.appKey;
    }

    public String language() {
        return this.language;
    }

    public String name() {
        return this.name;
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
            "sigma='" + this.sigma + '\'' +
            ", appId='" + this.appId + '\'' +
            ", appKey='" + this.appKey + '\'' +
            ", language='" + this.language + '\'' +
            ", name='" + this.name + '\'' +
            ", code='" + this.code + '\'' +
            '}';
    }
}
