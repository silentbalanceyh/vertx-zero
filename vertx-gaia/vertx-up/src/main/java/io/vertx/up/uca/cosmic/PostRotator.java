package io.vertx.up.uca.cosmic;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.config.IntegrationRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class PostRotator extends AbstractRotator {

    PostRotator(final Integration integration) {
        super(integration);
    }

    @Override
    public String request(final IntegrationRequest request, final JsonObject params) {
        /*
         * HttpPost
         * */
        final HttpPost httpPost = new HttpPost(this.configPath(request, params));
        final StringEntity body = this.dataJson(params);
        this.logger().info(INFO.HTTP_REQUEST, request.getPath(), request.getMethod(), params);
        return this.sendEntity(httpPost, body, request.getHeaders());
    }
}
