package io.vertx.up.runtime.develop;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class DiagnosisOption implements Serializable {
    // ------------- cache ----------------
    // 是否开启UI缓存，默认true，开启
    @JsonProperty("cache.ui")
    private Boolean cacheUi = Boolean.TRUE;

    // 是否开启认证，默认true，开启
    @JsonProperty("cache.authorized")
    private Boolean cacheAuthorized = Boolean.TRUE;

    // 是否打开授权缓存，默认true，开启
    @JsonProperty("cache.admit")
    private Boolean cacheAdmit = Boolean.TRUE;

    // ------------- secure ----------------
    // 是否显示密码，默认 false，不显示
    @JsonProperty("secure.password")
    private Boolean securePassword = Boolean.FALSE;

    // ------------- dev -------------------
    // 是否显示URI路由检测状况，默认 false，不检测
    @JsonProperty("dev.uri.detecting")
    private Boolean devUri = Boolean.FALSE;

    @JsonProperty("dev.stack.tracking")
    private Boolean devStack = Boolean.FALSE;

    @JsonProperty("dev.job.booting")
    private Boolean devJob = Boolean.FALSE;

    @JsonProperty("dev.excel.ranging")
    private Boolean devExcel = Boolean.FALSE;

    @JsonProperty("dev.jooq.querying")
    private Boolean devJooq = Boolean.FALSE;

    @JsonProperty("dev.expr.binding")
    private Boolean devExpression = Boolean.FALSE;

    @JsonProperty("dev.io.tracking")
    private Boolean devIo = Boolean.FALSE;
}
