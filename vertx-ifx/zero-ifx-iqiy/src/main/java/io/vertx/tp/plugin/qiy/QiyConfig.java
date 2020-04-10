package io.vertx.tp.plugin.qiy;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.feign.FeignDepot;
import io.vertx.up.log.Annal;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.Objects;

/**
 * For qiy client widely used.
 */
public class QiyConfig implements Serializable {

    private static final Annal LOGGER = Annal.get(QiyConfig.class);

    private static final String KEY = "qiy";
    private static final String KEY_ID = "client_id";
    private static final String KEY_SECRET = "client_secret";
    private static final String URL_UPLOAD = "url_upload";
    private static final String URL_QI_CHUAN = "url_qichuan";

    private static final String DFT_ENDPOINT = "https://openapi.iqiyi.com/api";
    private static final String DFT_UPLOAD = "http://upload.iqiyi.com";
    private static final String DFT_QI_CHUAN = "http://qichuan.iqiyi.com";

    private static final FeignDepot DEPOT = FeignDepot.create(KEY, KEY);
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String accessToken;
    private Long expires_in;

    private QiyConfig(final String clientId,
                      final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    static QiyConfig create(final String clientId,
                            final String clientSecret) {
        return new QiyConfig(clientId, clientSecret);
    }

    static QiyConfig create(final JsonObject config) {
        return new QiyConfig(config.getString(KEY_ID), config.getString(KEY_SECRET));
    }

    static QiyConfig create() {
        return create(DEPOT.getConfig());
    }

    public <T> T getUpApi(final Class<T> clazz) {
        return Fn.getSemi(!DEPOT.getConfig().containsKey(URL_UPLOAD), LOGGER,
                () -> DEPOT.build(clazz, DFT_UPLOAD));
    }

    public <T> T getHugeUpApi(final Class<T> clazz) {
        return Fn.getSemi(!DEPOT.getConfig().containsKey(URL_QI_CHUAN), LOGGER,
                () -> DEPOT.build(clazz, DFT_QI_CHUAN));
    }

    public <T> T getInitApi(final Class<T> clazz) {
        final String endpoint = DEPOT.getEndpoint();
        return DEPOT.build(clazz, null == endpoint ? DFT_ENDPOINT : endpoint);
    }

    public void clear(final String clientId, final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessToken = null;
        this.expires_in = 0L;
        this.refreshToken = null;
    }

    public void setToken(final JsonObject response) {
        Fn.safeNull(() -> {
            this.accessToken = response.getString("access_token");
            this.refreshToken = response.getString("refresh_token");
            // Fix Expire Field issue.
            this.expires_in = response.getLong("expires_in");
            if (null == this.expires_in) {
                this.expires_in = response.getLong("expiresIn");
            }
        }, response);
    }

    public boolean isValid() {
        return null != this.accessToken;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpires_in() {
        return this.expires_in;
    }

    public void setExpires_in(final long expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QiyConfig)) {
            return false;
        }
        final QiyConfig qiyRecord = (QiyConfig) o;
        return Objects.equals(this.getClientId(), qiyRecord.getClientId()) &&
                Objects.equals(this.getClientSecret(), qiyRecord.getClientSecret());
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getClientId(), this.getClientSecret());
    }
}
