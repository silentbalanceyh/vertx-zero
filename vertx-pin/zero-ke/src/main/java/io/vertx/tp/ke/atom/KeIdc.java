package io.vertx.tp.ke.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.commune.Json;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/*
 * Idc information for interface `Credential` specific
 */
public class KeIdc implements Serializable, Json {
    private transient String appId;
    private transient String sigma;
    private transient String realm;
    private transient String grantType;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }

    public String getRealm() {
        return this.realm;
    }

    public void setRealm(final String realm) {
        this.realm = realm;
    }

    public String getGrantType() {
        return this.grantType;
    }

    public void setGrantType(final String grantType) {
        this.grantType = grantType;
    }

    @Override
    public JsonObject toJson() {
        final JsonObject credential = new JsonObject();
        credential.put(KeField.SIGMA, this.sigma);
        credential.put(KeField.REALM, this.realm);
        credential.put(KeField.GRANT_TYPE, this.grantType);
        credential.put(KeField.APP_ID, this.appId);
        return credential;
    }

    @Override
    public void fromJson(final JsonObject json) {
        final JsonObject data = Ut.sureJObject(json);
        this.sigma = data.getString(KeField.SIGMA);
        this.appId = data.getString(KeField.APP_ID);
        this.realm = data.getString(KeField.REALM);
        this.grantType = data.getString(KeField.GRANT_TYPE);
    }
}
