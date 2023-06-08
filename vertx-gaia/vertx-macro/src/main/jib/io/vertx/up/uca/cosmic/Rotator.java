package io.vertx.up.uca.cosmic;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.IntegrationRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/*
 * Rotator for http request of major for methods:
 *
 * - POST
 * - GET
 * - PUT
 * - DELETE
 */
public interface Rotator {
    /*
     * The rotator could bind to HttpClient ( core )
     */
    Rotator bind(CloseableHttpClient client);

    /*
     * Request data with `Json` parameters, get string response
     */
    String request(IntegrationRequest request, JsonObject params);
}
