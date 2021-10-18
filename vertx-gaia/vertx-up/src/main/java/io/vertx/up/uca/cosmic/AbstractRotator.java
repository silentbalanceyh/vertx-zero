package io.vertx.up.uca.cosmic;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.config.IntegrationRequest;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.heart.JexlExpressionException;
import io.vertx.up.exception.web._500RequestConfigException;
import io.vertx.up.exception.web._501HttpClientNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractRotator implements Rotator {
    private final transient Integration integration;
    private transient CloseableHttpClient client;

    public AbstractRotator(final Integration integration) {
        this.integration = integration;
    }

    @Override
    public Rotator bind(final CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /*
     * The specific method for sub-class consume
     * - configRequest：configure `RequestConfig` object
     * - configPath：configure `path` in http request
     */
    protected RequestConfig configRequest() {
        final Integer connectTimeout =
            this.integration.getOption("timeout.connect", 5000);
        final Integer requestTimeout =
            this.integration.getOption("timeout.request", 5000);
        final Integer socketTimeout =
            this.integration.getOption("timeout.socket", 5000);
        return RequestConfig.custom()
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(requestTimeout)
            .setSocketTimeout(socketTimeout)
            .build();
    }

    protected String configPath(final IntegrationRequest request, final JsonObject params) {
        final String exprPath = request.getPath();
        if (request.isExpr()) {
            /*
             * The path contains `expression` such as
             *
             * `/path/${name}/user/${id}` mode
             *
             * In this situation, zero will parse this string
             */
            try {
                return request.getPath(params);
            } catch (final JexlExpressionException ex) {
                ex.printStackTrace();
                throw new _500RequestConfigException(this.getClass(), request, params);
            }
        } else {
            if (Ut.isNil(exprPath)) {
                throw new _500RequestConfigException(this.getClass(), request, params);
            } else {
                /*
                 * No paring
                 */
                return exprPath;
            }
        }
    }

    protected void configHeader(final HttpRequestBase request, final JsonObject headers) {
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }
        if (Ut.notNil(headers)) {
            headers.stream()
                .filter(item -> Objects.nonNull(item.getValue()))
                .forEach(item -> request.addHeader(item.getKey(), item.getValue().toString()));
        }
    }

    /*
     * Data processing method
     * - dataJson：Build json request of body, convert to StringEntity
     * - dataString：Build string content response here.
     */
    protected StringEntity dataJson(final JsonObject data) {
        final JsonObject normalized = Ut.sureJObject(data);

        /*
         *  Here the secondary argument will be in Chinese ??? confused characters.
         *  We must provide "utf8" as default encoding method of body
         *  */
        final StringEntity body = new StringEntity(normalized.encode(), Values.ENCODING);
        body.setContentEncoding(Values.ENCODING);
        body.setContentType(Values.CONTENT_TYPE);
        return body;
    }

    protected String dataString(final HttpResponse response) throws IOException {
        if (Objects.isNull(response)) {
            this.logger().info("Empty Http Response");
            return Strings.EMPTY;
        } else {
            final int statusLine = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusLine) {
                final String result = EntityUtils.toString(response.getEntity());
                this.logger().info("Success response: {0}", result);
                return result;
            } else {
                final String result = EntityUtils.toString(response.getEntity());
                this.logger().info("Failure response: {0}, code: {1}", result, statusLine);
                return result;
            }
        }
    }

    protected CloseableHttpClient client() {
        if (Objects.isNull(this.client)) {
            throw new _501HttpClientNullException(this.getClass());
        }
        return this.client;
    }

    /*
     * Send request with body or without body
     * - sendEntity
     * - sendUrl
     */
    protected String sendEntity(final HttpEntityEnclosingRequestBase request,
                                final HttpEntity body,
                                final JsonObject headers) {
        /* RequestConfig */
        final RequestConfig config = this.configRequest();
        request.setConfig(config);
        request.setEntity(body);

        /* default http header */
        this.configHeader(request, headers);

        /* send request */
        return Fn.getJvm(() -> {
            final HttpResponse httpResponse = this.client().execute(request);

            /* build response here */
            return this.dataString(httpResponse);
        });
    }

    protected String sendUrl(final HttpRequestBase request, final JsonObject headers) {
        /* RequestConfig */
        final RequestConfig config = this.configRequest();
        request.setConfig(config);

        /* default http header */
        this.configHeader(request, headers);

        /* send request */
        return Fn.getJvm(() -> {
            final HttpResponse httpResponse = this.client().execute(request);

            /* Final data */
            return this.dataString(httpResponse);
        });
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
