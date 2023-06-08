package io.vertx.up.runtime.env;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.horizon.runtime.Macrocosm;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * 调试专用环境变量（统一处理）
 * 基本命名规范
 * - Z_DEV_:    开发专用参数
 * - Z_CACHE_:  缓存相关参数
 * 检索优先级
 * 1. 先检查环境变量
 * 2. 再检查配置中的信息，配置格式如下
 * <pre><code class="yaml">
 *     # vertx-deployment.yml
 *     development:
 *       ENV:
 *         Z_DEV_XX: xxx
 * </code></pre>
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

    // Z_CACHE_UI
    public Boolean getCacheUi() {
        return Ut.envWith(CACHE_UI, this.cacheUi, Boolean.class);
    }

    public void setCacheUi(final Boolean cacheUi) {
        this.cacheUi = cacheUi;
    }

    // Z_CACHE_ADMIT
    public Boolean getCacheAdmit() {
        return Ut.envWith(CACHE_ADMIT, this.cacheAdmit, Boolean.class);
    }

    public void setCacheAdmit(final Boolean cacheAdmit) {
        this.cacheAdmit = cacheAdmit;
    }

    // Z_DEV_AUTHORIZED
    public Boolean getDevAuthorized() {
        return Ut.envWith(DEV_AUTHORIZED, this.devAuthorized, Boolean.class);
    }

    public void setDevAuthorized(final Boolean devAuthorized) {
        this.devAuthorized = devAuthorized;
    }

    // Z_DEV_WEB_URI
    public Boolean getDevWebUri() {
        return Ut.envWith(DEV_WEB_URI, this.devWebUri, Boolean.class);
    }

    public void setDevWebUri(final Boolean devWebUri) {
        this.devWebUri = devWebUri;
    }

    // Z_DEV_JVM_STACK
    public Boolean getDevJvmStack() {
        return Ut.envWith(DEV_JVM_STACK, this.devJvmStack, Boolean.class);
    }

    public void setDevJvmStack(final Boolean devJvmStack) {
        this.devJvmStack = devJvmStack;
    }

    // Z_DEV_JOB_BOOT
    public Boolean getDevJobBoot() {
        return Ut.envWith(DEV_JOB_BOOT, this.devJobBoot, Boolean.class);
    }

    public void setDevJobBoot(final Boolean devJobBoot) {
        this.devJobBoot = devJobBoot;
    }

    // Z_DEV_EXCEL_RANGE
    public Boolean getDevExcelRange() {
        return Ut.envWith(DEV_EXCEL_RANGE, this.devExcelRange, Boolean.class);
    }

    public void setDevExcelRange(final Boolean devExcelRange) {
        this.devExcelRange = devExcelRange;
    }

    // Z_DEV_JOOQ_COND
    public Boolean getDevJooqCond() {
        return Ut.envWith(DEV_JOOQ_COND, this.devJooqCond, Boolean.class);
    }

    public void setDevJooqCond(final Boolean devJooqCond) {
        this.devJooqCond = devJooqCond;
    }

    // Z_DEV_EXPR_BIND
    public Boolean getDevExprBind() {
        return Ut.envWith(DEV_EXPR_BIND, this.devExprBind, Boolean.class);
    }

    public void setDevExprBind(final Boolean devExprBind) {
        this.devExprBind = devExprBind;
    }

    // Z_DEV_DAO_BIND
    public Boolean getDevDaoBind() {
        return Ut.envWith(DEV_DAO_BIND, this.devDaoBind, Boolean.class);
    }

    public void setDevDaoBind(final Boolean devDaoBind) {
        this.devDaoBind = devDaoBind;
    }
}
