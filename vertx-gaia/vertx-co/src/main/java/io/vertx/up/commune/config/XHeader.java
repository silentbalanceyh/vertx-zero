package io.vertx.up.commune.config;

import io.horizon.specification.typed.TJson;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

public class XHeader implements Serializable, TJson {

    private String sigma;
    private String appId;
    private String appKey;
    private String language;
    // New for Cloud
    private String tenantId;
    private String session;

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(final String appKey) {
        this.appKey = appKey;
    }

    public String session() {
        return this.session;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public void fromJson(final JsonObject json) {
        final XHeader header = Ut.deserialize(json, XHeader.class);
        if (Objects.nonNull(header)) {
            this.appId = header.appId;
            this.appKey = header.appKey;
            this.sigma = header.sigma;
            this.language = header.language;
            this.session = header.session;
            this.tenantId = header.tenantId;
        }
    }

    public void fromHeader(final MultiMap headers) {
        if (Objects.nonNull(headers)) {
            this.appId = headers.get(KWeb.HEADER.X_APP_ID);
            this.appKey = headers.get(KWeb.HEADER.X_APP_KEY);
            this.sigma = headers.get(KWeb.HEADER.X_SIGMA);
            this.language = headers.get(KWeb.HEADER.X_LANG);
            this.session = headers.get(KWeb.HEADER.X_SESSION_ID);
            this.tenantId = headers.get(KWeb.HEADER.X_TENANT_ID);
        }
    }

    @Override
    public JsonObject toJson() {
        return Ut.serializeJson(this);
    }
}
