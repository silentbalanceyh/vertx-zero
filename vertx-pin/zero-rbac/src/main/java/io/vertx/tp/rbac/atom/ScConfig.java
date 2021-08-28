package io.vertx.tp.rbac.atom;

import java.io.Serializable;

/*
 * Security configuration data
 *
 */
public class ScConfig implements Serializable {
    /*
     * Unique condition for Security Entity
     * 1) User
     * 2) Role
     * 3) Group
     * 4) Permission
     * 5) Action
     * 6) Resource
     */
    private ScCondition condition;
    /*
     * Authorization Code expired time: ( s )
     */
    private Integer codeExpired;
    /*
     * Authorization Code length ( random string )
     */
    private Integer codeLength;
    /*
     * Authorization Code session pool
     */
    private String codePool;
    /*
     * Token expired time: ( ms )
     */
    private Long tokenExpired;
    /*
     * Token session pool
     */
    private String tokenPool;
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
    /*
     * Role Pool when secondary cache enabled.
     */
    private String permissionPool;
    /*
     * Password Init
     */
    private String passwordInit;

    public String getPasswordInit() {
        return this.passwordInit;
    }

    public void setPasswordInit(final String passwordInit) {
        this.passwordInit = passwordInit;
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

    public String getCodePool() {
        return this.codePool;
    }

    public void setCodePool(final String codePool) {
        this.codePool = codePool;
    }

    public Boolean getSupportSecondary() {
        return this.supportSecondary;
    }

    public void setSupportSecondary(final Boolean supportSecondary) {
        this.supportSecondary = supportSecondary;
    }

    public String getPermissionPool() {
        return this.permissionPool;
    }

    public void setPermissionPool(final String permissionPool) {
        this.permissionPool = permissionPool;
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

    public String getTokenPool() {
        return this.tokenPool;
    }

    public void setTokenPool(final String tokenPool) {
        this.tokenPool = tokenPool;
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

    @Override
    public String toString() {
        return "ScConfig{" +
            "condition=" + this.condition +
            ", codeExpired=" + this.codeExpired +
            ", codeLength=" + this.codeLength +
            ", codePool='" + this.codePool + '\'' +
            ", tokenExpired=" + this.tokenExpired +
            ", tokenPool='" + this.tokenPool + '\'' +
            ", supportGroup=" + this.supportGroup +
            ", supportSecondary=" + this.supportSecondary +
            ", supportMultiApp=" + this.supportMultiApp +
            ", permissionPool='" + this.permissionPool + '\'' +
            ", passwordInit='" + this.passwordInit + '\'' +
            '}';
    }
}
