package io.aeon.experiment.specification;

import io.horizon.specification.typed.TJson;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/*
 * Idc information for interface `Credential` specific
 */
public class KCredential implements Serializable, TJson {
    private transient String appId;
    private transient String sigma;
    private transient String language;
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

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    @Override
    public JsonObject toJson() {
        final JsonObject credential = new JsonObject();
        credential.put(KName.SIGMA, this.sigma);
        credential.put(KName.REALM, this.realm);
        credential.put(KName.GRANT_TYPE, this.grantType);
        credential.put(KName.APP_ID, this.appId);
        credential.put(KName.LANGUAGE, this.language);
        return credential;
    }

    @Override
    public void fromJson(final JsonObject json) {
        final JsonObject data = Ut.valueJObject(json);
        this.sigma = data.getString(KName.SIGMA);
        this.appId = data.getString(KName.APP_ID);
        this.realm = data.getString(KName.REALM);
        this.language = data.getString(KName.LANGUAGE);
        this.grantType = data.getString(KName.GRANT_TYPE);
    }
}
