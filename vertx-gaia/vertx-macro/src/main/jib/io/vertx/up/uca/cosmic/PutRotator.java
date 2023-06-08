package io.vertx.up.uca.cosmic;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.config.IntegrationRequest;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class PutRotator extends AbstractRotator {

    PutRotator(final Integration integration) {
        super(integration);
    }

    @Override
    public String request(final IntegrationRequest request, final JsonObject params) {
        /*
         * HttpPut
         */
        final HttpPut httpPut = new HttpPut(this.configPath(request, params));
        final StringEntity body = this.dataJson(params);
        this.logger().info(INFO.HTTP_REQUEST, request.getPath(), request.getMethod(), params);
        return this.sendEntity(httpPut, body, request.getHeaders());
    }
}
