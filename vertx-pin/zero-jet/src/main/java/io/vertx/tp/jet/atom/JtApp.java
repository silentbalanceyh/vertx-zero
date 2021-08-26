package io.vertx.tp.jet.atom;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;

import java.io.Serializable;

/*
 * XHeader for current jet here.
 */
public class JtApp implements Serializable {
    /* appId, appKey, sigma */
    private transient String appId;
    private transient String appKey;
    private transient String sigma;

    /* name, code, language, active */
    private transient String name;
    private transient String code;
    private transient String language;
    private transient Boolean active;

    /* logo, title */
    private transient String logo;
    private transient String title;

    /* business -> icp, email, copyRight */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject business;

    /* backend -> domain, appPort, route */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject backend;

    /* frontend -> path, urlEntry, urlMain */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject frontend;

    /* auditor -> createdAt, createdBy, updatedAt, updatedBy */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject auditor;

    /*
     * database
     * - hostname, instance, port, category
     * - jdbcUrl, username, password
     * - jdbcConfig
     * */
    private transient Database source;

    @Override
    public String toString() {
        return "JtEnv{" +
            "appId='" + this.appId + '\'' +
            ", appKey='" + this.appKey + '\'' +
            ", sigma='" + this.sigma + '\'' +
            ", name='" + this.name + '\'' +
            ", code='" + this.code + '\'' +
            ", language='" + this.language + '\'' +
            ", active=" + this.active +
            ", logo='" + this.logo + '\'' +
            ", title='" + this.title + '\'' +
            ", business=" + this.business +
            ", backend=" + this.backend +
            ", frontend=" + this.frontend +
            ", auditor=" + this.auditor +
            ", source=" + this.source +
            '}';
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

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setBusiness(final JsonObject business) {
        this.business = business;
    }

    public void setBackend(final JsonObject backend) {
        this.backend = backend;
    }

    public void setFrontend(final JsonObject frontend) {
        this.frontend = frontend;
    }

    public void setAuditor(final JsonObject auditor) {
        this.auditor = auditor;
    }

    /*
     * Defined Method
     */
    public String getRoute() {
        return this.backend.getString(KName.App.ROUTE);
    }

    public Database getSource() {
        return this.source;
    }

    public void setSource(final Database source) {
        this.source = source;
    }
}
