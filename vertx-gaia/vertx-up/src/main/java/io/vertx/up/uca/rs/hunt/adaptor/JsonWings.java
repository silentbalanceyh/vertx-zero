package io.vertx.up.uca.rs.hunt.adaptor;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class JsonWings extends AbstractWings {
    @Override
    public void output(final HttpServerResponse response, final Envelop envelop) {
        if (this.isFreedom()) {
            final String content = this.toFreedom(envelop);
            if (Objects.isNull(content)) {
                /*
                 * No Content automatic
                 */
                response.setStatusCode(HttpStatusCode.NO_CONTENT.code());
                response.setStatusMessage(HttpStatusCode.NO_CONTENT.message());
                response.end(Strings.EMPTY);
            } else {
                /*
                 * Freedom successful
                 */
                this.logger().info("Freedom mode enabled successfully.");
                response.end(content);
            }
        } else {
            /*
             * Default String mode
             * 1. Content-Type is `* / *` format
             * 2. Replied body directly
             */
            response.end(envelop.outString());
        }
    }
}
