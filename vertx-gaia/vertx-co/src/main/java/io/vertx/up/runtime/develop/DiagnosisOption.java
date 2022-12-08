package io.vertx.up.runtime.develop;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.up.runtime.Macrocosm;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * 调试专用环境变量（统一处理）
 * 基本命名规范
 * - dev:    开发专用参数
 * - cache:  缓存相关参数
 * - secure: 安全相关参数
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DiagnosisOption implements Serializable, Macrocosm {
    // ------------- cache ----------------
    // 是否开启UI缓存，默认true，开启
    @JsonProperty(CACHE_UI)
    private Boolean cacheUi = Boolean.TRUE;

    // 是否打开授权缓存，默认true，开启
    @JsonProperty(CACHE_ADMIT)
    private Boolean cacheAdmit = Boolean.TRUE;
    // ------------- dev -------------------
    // 是否显示URI路由检测状况，默认 false，不检测
    @JsonProperty(DEV_WEB_URI)
    private Boolean devWebUri = Boolean.FALSE;
    @JsonProperty(DEV_JVM_STACK)
    private Boolean devJvmStack = Boolean.FALSE;
    @JsonProperty(DEV_JOB_BOOT)
    private Boolean devJobBoot = Boolean.FALSE;
    @JsonProperty(DEV_EXCEL_RANGE)
    private Boolean devExcelRange = Boolean.FALSE;
    @JsonProperty(DEV_JOOQ_COND)
    private Boolean devJooqCond = Boolean.FALSE;
    @JsonProperty(DEV_EXPR_BIND)
    private Boolean devExprBind = Boolean.FALSE;
    @JsonProperty(DEV_DAO_BIND)
    private Boolean devDaoBind = Boolean.TRUE;

    // 是否开启认证日志，默认false，关闭（5个地方调用）
    @JsonProperty(DEV_AUTHORIZED)
    private Boolean devAuthorized = Boolean.FALSE;

    public Boolean getCacheUi() {
        return Ut.envIn(CACHE_UI, this.cacheUi, Boolean.class);
    }

    public void setCacheUi(final Boolean cacheUi) {
        this.cacheUi = cacheUi;
    }

    public Boolean getCacheAdmit() {
        return this.cacheAdmit;
    }

    public void setCacheAdmit(final Boolean cacheAdmit) {
        this.cacheAdmit = cacheAdmit;
    }

    public Boolean getDevAuthorized() {
        return this.devAuthorized;
    }

    public void setDevAuthorized(final Boolean devAuthorized) {
        this.devAuthorized = devAuthorized;
    }

    public Boolean getDevWebUri() {
        return this.devWebUri;
    }

    public void setDevWebUri(final Boolean devWebUri) {
        this.devWebUri = devWebUri;
    }

    public Boolean getDevJvmStack() {
        return this.devJvmStack;
    }

    public void setDevJvmStack(final Boolean devJvmStack) {
        this.devJvmStack = devJvmStack;
    }

    public Boolean getDevJobBoot() {
        return this.devJobBoot;
    }

    public void setDevJobBoot(final Boolean devJobBoot) {
        this.devJobBoot = devJobBoot;
    }

    public Boolean getDevExcelRange() {
        return this.devExcelRange;
    }

    public void setDevExcelRange(final Boolean devExcelRange) {
        this.devExcelRange = devExcelRange;
    }

    public Boolean getDevJooqCond() {
        return this.devJooqCond;
    }

    public void setDevJooqCond(final Boolean devJooqCond) {
        this.devJooqCond = devJooqCond;
    }

    public Boolean getDevExprBind() {
        return this.devExprBind;
    }

    public void setDevExprBind(final Boolean devExprBind) {
        this.devExprBind = devExprBind;
    }

    public Boolean getDevDaoBind() {
        return this.devDaoBind;
    }

    public void setDevDaoBind(final Boolean devDaoBind) {
        this.devDaoBind = devDaoBind;
    }
}
