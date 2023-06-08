package io.vertx.up.uca.cosmic;

import io.horizon.eon.VString;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.config.IntegrationRequest;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class LegacyEmitter extends AbstractEmitter {
    LegacyEmitter(final Integration integration) {
        super(integration);
    }

    /*
     * Set trusted connect
     */
    @Override
    protected void initialize() {
        final SSLContext context = this.sslContext();
        if (Objects.nonNull(context)) {

            /* Initialize HttpsURLConnection */
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        }
    }

    @Override
    public String request(final String apiKey, final JsonObject params, final MultiMap headers) {
        return Fn.runOr(VString.EMPTY, () -> {
            /*
             * Read IntegrationRequest object
             */
            final IntegrationRequest request = this.integration().createRequest(apiKey);
            /*
             * Encrypt content with public key of RSA
             * Replace the method `getPublicKeyFile` with `getPublicKey` for content extracting
             */
            final String content = Ut.encryptRSAP(params.encode(), this.integration().getPublicKey());
            /*
             * Send request to read String response here.
             */
            return this.send(request.getPath(), request.getMethod(), MediaType.APPLICATION_JSON_TYPE, content);
        }, params, apiKey);
    }

    private String send(final String uri, final HttpMethod method, final MediaType mediaType, final String content) {
        return Fn.failOr(null, () -> {
            this.logger().info(INFO.HTTP_REQUEST, uri, method, content);
            final String contentType = Objects.isNull(mediaType) ? MediaType.APPLICATION_JSON : mediaType.toString();

            /* Cert trusted */
            this.initialize();

            /* Create new connect */
            final URL url = new URL(uri);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            /* Set options for current connection */
            conn.setRequestMethod(method.name());
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            /* Input resonse stream to String */
            final PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(content);
            out.flush();
            out.close();

            /* Convert to content */
            final String response = Ut.ioString(conn.getInputStream());
            final String normalized = new String(response.getBytes(), StandardCharsets.UTF_8);
            this.logger().info(INFO.HTTP_RESPONSE, normalized);
            return normalized;
        }, uri, method, content);
    }
}
