package io.vertx.tp.plugin.ali.sms;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.init.TpConfig;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.io.Serializable;
import java.util.Objects;

public class SmsConfig implements Serializable {

    static final String TIMEOUT_CONN = "timeout_connect";
    static final String TIMEOUT_READ = "timeout_read";
    static final String DFT_PRODUCT = "Dysmsapi";
    static final String DFT_REGION = "cn-hangzhou";
    static final String RESPONSE_REQUEST_ID = "request_id";
    static final String RESPONSE_BUSINESS_ID = "business_id";
    static final String RESPONSE_CODE = "code";
    static final String RESPONSE_MESSAGE = "message";
    private static final String KEY = "ali-sms";
    private static final String KEY_ID = "access_id";
    private static final String KEY_SECRET = "access_secret";
    private static final String KEY_SIGN_NAME = "sign_name";
    private static final String KEY_TPL = "tpl";
    private static final String DFT_DOMAIN = "dysmsapi.aliyuncs.com";

    private static final TpConfig CONFIG = TpConfig.create(KEY, KEY);

    private final String accessId;
    private final String accessSecret;
    private final String signName;
    private final JsonObject tpl;
    private String endpoint;

    private SmsConfig(final String accessId, final String accessSecret, final String signName, final JsonObject tpl) {
        this.accessId = accessId;
        this.accessSecret = accessSecret;
        this.signName = signName;
        this.tpl = tpl;
        this.endpoint = CONFIG.getEndPoint();
        if (null == this.endpoint) {
            this.endpoint = DFT_DOMAIN;
        }
    }

    static SmsConfig create(final String accessId,
                            final String accessSecret,
                            final String signName) {
        return new SmsConfig(accessId, accessSecret, signName, null);
    }

    static SmsConfig create(final String accessId,
                            final String accessSecret,
                            final String signName,
                            final JsonObject tpl) {
        return new SmsConfig(accessId, accessSecret, signName, tpl);
    }

    static SmsConfig create(final JsonObject config) {
        return new SmsConfig(config.getString(KEY_ID), config.getString(KEY_SECRET), config.getString(KEY_SIGN_NAME), config.getJsonObject(KEY_TPL));
    }

    static SmsConfig create() {
        return create(CONFIG.getConfig());
    }

    public JsonObject getConfig() {
        return CONFIG.getConfig();
    }

    public String getAccessId() {
        return this.accessId;
    }

    public String getAccessSecret() {
        return this.accessSecret;
    }

    public String getSignName() {
        return this.signName;
    }

    public String getDomain() {
        return this.endpoint;
    }

    @SuppressWarnings("all")
    public String getTpl(final String key) {
        return Fn.runOr(null != this.tpl && this.tpl.containsKey(key), Annal.get(this.getClass()),
            () -> this.tpl.getString(key));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsConfig)) {
            return false;
        }
        final SmsConfig smsConfig = (SmsConfig) o;
        return Objects.equals(this.accessId, smsConfig.accessId) &&
            Objects.equals(this.accessSecret, smsConfig.accessSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.accessId, this.accessSecret);
    }
}
