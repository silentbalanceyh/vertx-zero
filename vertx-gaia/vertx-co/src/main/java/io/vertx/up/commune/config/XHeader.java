package io.vertx.up.commune.config;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Json;
import io.vertx.up.eon.ID;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

public class XHeader implements Serializable, Json {

    private String sigma;
    private String appId;
    private String appKey;
    private String language;
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

    @Override
    public void fromJson(final JsonObject json) {
        final XHeader header = Ut.deserialize(json, XHeader.class);
        if (Objects.nonNull(header)) {
            this.appId = header.appId;
            this.appKey = header.appKey;
            this.sigma = header.sigma;
            this.language = header.language;
            this.session = header.session;
        }
    }

    public void fromHeader(final MultiMap headers) {
        if (Objects.nonNull(headers)) {
            this.appId = headers.get(ID.Header.X_APP_ID);
            this.appKey = headers.get(ID.Header.X_APP_KEY);
            this.sigma = headers.get(ID.Header.X_SIGMA);
            this.language = headers.get(ID.Header.X_LANG);
            this.session = headers.get(ID.Header.X_SESSION_ID);
        }
    }

    @Override
    public JsonObject toJson() {
        return Ut.serializeJson(this);
    }
}
