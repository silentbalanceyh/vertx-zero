package io.vertx.up.uca.cosmic;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.config.IntegrationRequest;
import io.vertx.up.util.Ut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.util.Objects;
import java.util.function.Function;

class StandardEmitter extends AbstractEmitter {

    private transient CloseableHttpClient client;

    StandardEmitter(final Integration integration) {
        super(integration);
        /* Call initialize here */
        this.initialize();
    }

    @Override
    protected void initialize() {
        final SSLContext sslcontext = this.sslContext();
        /*
         * RegistryBuilder
         */
        final RegistryBuilder<ConnectionSocketFactory> registry = RegistryBuilder.create();
        registry.register(VPath.PROTOCOL.HTTP, PlainConnectionSocketFactory.INSTANCE);
        if (Objects.nonNull(sslcontext)) {
            registry.register(VPath.PROTOCOL.HTTPS, new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE));
        }

        /*
         * Connection Pool，create new client here
         */
        final Registry<ConnectionSocketFactory> factory = registry.build();
        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(factory);
        this.client = HttpClients.custom().setConnectionManager(connManager).build();
    }

    @Override
    public String request(final String apiKey, final JsonObject params, final MultiMap headers) {
        /*
         * Read IntegrationRequest object
         */
        final IntegrationRequest request = this.integration().createRequest(apiKey);
        if (Objects.nonNull(headers)) {
            request.setHeaders(Ut.toJObject(headers));
        }
        /*
         * Build Rotator instances
         */
        final Function<Integration, Rotator> executor = CACHE.POOL_ROTATOR_FN.get(request.getMethod());
        if (Objects.isNull(executor)) {
            return VString.EMPTY;
        } else {
            /*
             * Cached rotator, the default is integration request definition.
             */
            final Rotator rotator = CACHE.CC_ROTATOR.pick(
                () -> executor.apply(this.integration()).bind(this.client), request.hashCode());
            // Fn.po?l(Pool.POOL_ROTATOR, request.hashCode(), () -> executor.apply(this.integration()).bind(this.client));
            /*
             * 执行请求
             */
            return rotator.request(request, params);
        }
    }
}
