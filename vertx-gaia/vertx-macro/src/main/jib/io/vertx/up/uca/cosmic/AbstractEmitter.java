package io.vertx.up.uca.cosmic;

import io.horizon.uca.log.Annal;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.em.EmTraffic;
import io.vertx.up.util.Ut;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
 * 核心内容
 * 1 - 认证相关，从 integration 中计算
 * 2 - 两种模式，HTTP 和 HTTPS
 */
public abstract class AbstractEmitter implements Emitter {

    private final transient Integration integration;

    public AbstractEmitter(final Integration integration) {
        this.integration = integration;
    }

    /*
     * 初始化 SSLContext
     */
    protected SSLContext sslContext() {
        /* 创建一个不验证证书的信任管理器 */
        final TrustManager[] trustCerts = new TrustManager[]{new TrustX509()};

        /* 设置完全信任的信任管理器 */
        /*
         * 直接在 Integration 中设置
         * options:
         * {
         *     "https": "TLS"
         * }
         */
        final String sslMode = this.integration.getOption("https", EmTraffic.Https.TLS.name());
        final EmTraffic.Https type = Ut.toEnum(() -> sslMode, EmTraffic.Https.class, EmTraffic.Https.TLS);
        try {
            final SSLContext context = SSLContext.getInstance(type.name());
            context.init(null, trustCerts, new SecureRandom());
            return context;
        } catch (final NoSuchAlgorithmException | KeyManagementException ex) {
            this.logger().fatal(ex);
            return null;
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    protected Integration integration() {
        return this.integration;
    }

    protected abstract void initialize();

    @Override
    public JsonObject requestJ(final String apiKey, final JsonObject params, final MultiMap headers) {
        final String content = this.request(apiKey, params, headers);
        return Ut.isJObject(content) ? Ut.toJObject(content) : new JsonObject();
    }

    @Override
    public JsonArray requestA(final String apiKey, final JsonObject params, final MultiMap headers) {
        final String content = this.request(apiKey, params, headers);
        return Ut.isJArray(content) ? Ut.toJArray(content) : new JsonArray();
    }
}
