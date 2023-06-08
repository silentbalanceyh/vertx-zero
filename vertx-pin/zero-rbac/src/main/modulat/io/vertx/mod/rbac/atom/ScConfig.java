package io.vertx.mod.rbac.atom;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aeon.experiment.specification.KQr;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/*
 * Security configuration data
 *
 */
public class ScConfig implements Serializable {
    private static final Cc<String, KQr> CC_KQR = Cc.open();
    /*
     * Pool for verify the code
     */
    private final String poolVerify = AuthKey.Pool.VERIFY;
    /*
     * Pool for login limitation here
     */
    private final String poolLimitation = AuthKey.Pool.LIMITATION;
    /*
     * Unique condition for Security Entity
     * 1) User
     * 2) Role
     * 3) Group
     * 4) Permission
     * 5) Action
     * 6) Resource
     */
    private ScCondition condition = new ScCondition();
    /*
     * Authorization Code expired time: ( s )
     */
    private Integer codeExpired = 30;
    /*
     * Authorization Code length ( random string )
     */
    private Integer codeLength = 8;
    /*
     * Authorization Code session pool
     */
    private String poolCode = AuthKey.Pool.CODE;
    /*
     * Token expired time: ( ms )
     */
    private Long tokenExpired = 30L;
    /*
     * Token session pool
     */
    private String poolToken = AuthKey.Pool.TOKEN;
    /*
     * Enable user group feature
     */
    private Boolean supportGroup = Boolean.FALSE;
    /*
     * Enable secondary cache for permissions ( role = xxx )
     */
    private Boolean supportSecondary = Boolean.FALSE;
    /*
     * Enable multi application, whether search action with X-Sigma Header
     */
    private Boolean supportMultiApp = Boolean.TRUE;

    private Boolean supportIntegration = Boolean.FALSE;
    /*
     * Enable image code here, if enabled, the login component must be
     * from `ExLogin` switched to `ExEntry` instead, because here need
     * additional parameters `verifyCode` instead password only.
     */
    private Boolean verifyCode = Boolean.FALSE;
    /*
     * Enable login counter limitation.
     */
    private Integer verifyLimitation = null;
    /*
     * When verifyLimitation is not null, this means the duration when counter ended.
     * For example:
     * 1. verifyLimitation = 3
     * 2. verifyDuration = 300
     * It means 3 max login handler times per 300 seconds.
     * 3 times / 300 seconds
     */
    private Integer verifyDuration = 300;
    /* Role Pool when secondary cache enabled. */
    private String poolPermission = AuthKey.Pool.PERMISSIONS;
    /* Resource Pool when secondary cache enabled. */
    private String poolResource = AuthKey.Pool.RESOURCES;
    /* Resource Pool when admit for RBAC Management */
    private String poolAdmit = AuthKey.Pool.ADMIT;
    /*
     * Password Init
     */
    private String initializePassword;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject initialize = new JsonObject();

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject category = new JsonObject();

    public String getInitializePassword() {
        return this.initializePassword;
    }

    public void setInitializePassword(final String initializePassword) {
        this.initializePassword = initializePassword;
    }

    public ScCondition getCondition() {
        return this.condition;
    }

    public void setCondition(final ScCondition condition) {
        this.condition = condition;
    }

    public Integer getCodeExpired() {
        return this.codeExpired;
    }

    public void setCodeExpired(final Integer codeExpired) {
        this.codeExpired = codeExpired;
    }

    public Integer getCodeLength() {
        return this.codeLength;
    }

    public void setCodeLength(final Integer codeLength) {
        this.codeLength = codeLength;
    }


    public Boolean getSupportSecondary() {
        return this.supportSecondary;
    }

    public void setSupportSecondary(final Boolean supportSecondary) {
        this.supportSecondary = supportSecondary;
    }

    public String getPoolCode() {
        return this.poolCode;
    }

    public void setPoolCode(final String poolCode) {
        this.poolCode = poolCode;
    }

    public String getPoolPermission() {
        return this.poolPermission;
    }

    public void setPoolPermission(final String poolPermission) {
        this.poolPermission = poolPermission;
    }

    public String getPoolAdmit() {
        return this.poolAdmit;
    }

    public void setPoolAdmit(final String poolAdmit) {
        this.poolAdmit = poolAdmit;
    }

    public Long getTokenExpired() {
        if (null == this.tokenExpired) {
            this.tokenExpired = 0L;
        }
        /* To ms */
        return this.tokenExpired * 1000 * 1000;
    }

    public void setTokenExpired(final Long tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Boolean getVerifyCode() {
        return this.verifyCode;
    }

    public void setVerifyCode(final Boolean verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Integer getVerifyLimitation() {
        return this.verifyLimitation;
    }

    public void setVerifyLimitation(final Integer verifyLimitation) {
        this.verifyLimitation = verifyLimitation;
    }

    public Integer getVerifyDuration() {
        return this.verifyDuration;
    }

    public void setVerifyDuration(final Integer verifyDuration) {
        this.verifyDuration = verifyDuration;
    }

    public String getPoolVerify() {
        return this.poolVerify;
    }

    public String getPoolLimitation() {
        return this.poolLimitation;
    }

    public String getPoolToken() {
        return this.poolToken;
    }

    public void setPoolToken(final String poolToken) {
        this.poolToken = poolToken;
    }

    public Boolean getSupportGroup() {
        return this.supportGroup;
    }

    public void setSupportGroup(final Boolean supportGroup) {
        this.supportGroup = supportGroup;
    }

    public Boolean getSupportMultiApp() {
        return this.supportMultiApp;
    }

    public void setSupportMultiApp(final Boolean supportMultiApp) {
        this.supportMultiApp = supportMultiApp;
    }

    public String getPoolResource() {
        return this.poolResource;
    }

    public void setPoolResource(final String poolResource) {
        this.poolResource = poolResource;
    }

    public JsonObject getCategory() {
        return this.category;
    }

    public void setCategory(final JsonObject category) {
        this.category = category;
    }

    public Boolean getSupportIntegration() {
        return this.supportIntegration;
    }

    public void setSupportIntegration(final Boolean supportIntegration) {
        this.supportIntegration = supportIntegration;
    }

    public KQr category(final String name) {
        return CC_KQR.pick(() -> {
            final JsonObject serializeJ = Ut.valueJObject(this.category, name);
            final KQr qr = Ut.deserialize(serializeJ, KQr.class);
            if (qr.valid()) {
                return qr.identifier(name);
            } else {
                return null;
            }
        }, name);
    }

    public JsonObject getInitialize() {
        return this.initialize;
    }

    public void setInitialize(final JsonObject initialize) {
        this.initialize = initialize;
    }

    @Override
    public String toString() {
        return "ScConfig{" +
            "poolVerify='" + this.poolVerify + '\'' +
            ", poolLimitation='" + this.poolLimitation + '\'' +
            ", condition=" + this.condition +
            ", codeExpired=" + this.codeExpired +
            ", codeLength=" + this.codeLength +
            ", poolCode='" + this.poolCode + '\'' +
            ", tokenExpired=" + this.tokenExpired +
            ", poolToken='" + this.poolToken + '\'' +
            ", supportGroup=" + this.supportGroup +
            ", supportSecondary=" + this.supportSecondary +
            ", supportMultiApp=" + this.supportMultiApp +
            ", supportIntegration=" + this.supportIntegration +
            ", verifyCode=" + this.verifyCode +
            ", verifyLimitation=" + this.verifyLimitation +
            ", verifyDuration=" + this.verifyDuration +
            ", poolPermission='" + this.poolPermission + '\'' +
            ", poolResource='" + this.poolResource + '\'' +
            ", initializePassword='" + this.initializePassword + '\'' +
            ", initialize=" + this.initialize +
            ", category=" + this.category +
            '}';
    }
}
