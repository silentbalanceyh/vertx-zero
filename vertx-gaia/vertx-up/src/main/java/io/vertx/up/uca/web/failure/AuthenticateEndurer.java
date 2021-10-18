package io.vertx.up.uca.web.failure;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.hunt.Answer;
import io.vertx.up.unity.Ux;

/**
 * Common handler to handle failure
 */
public class AuthenticateEndurer implements Handler<RoutingContext> {

    private static final Annal LOGGER = Annal.get(AuthenticateEndurer.class);

    private AuthenticateEndurer() {
    }

    public static Handler<RoutingContext> create() {
        return new AuthenticateEndurer();
    }

    @Override
    public void handle(final RoutingContext event) {
        if (event.failed()) {
            final Throwable ex = event.failure();
            if (ex instanceof WebException) {
                LOGGER.info("Web Exception: {0} = {1}", ex.getClass().getName(), ex.getMessage());
                final WebException error = (WebException) ex;
                /*
                 * XHeader bind
                 */
                Ux.debug(error, () -> error);
                Answer.reply(event, Envelop.failure(error));
            } else {
                // Other exception found
                LOGGER.info("Exception: {0} = {1}", ex.getClass().getName(), ex.getMessage());
                ex.printStackTrace();
                Answer.reply(event, Envelop.failure(ex));
            }
        } else {
            // Success, do not throw, continue to request
            event.next();
        }
    }
}
