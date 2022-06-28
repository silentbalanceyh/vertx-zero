package io.vertx.tp.plugin.stomp.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.ext.stomp.StompServerConnection;

import java.util.Objects;

/**
 * This handler must be mount to Default because of the STOMP must require
 * zero-rbac connect instead of
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BuiltInConnectHandler extends AbstractBuiltInHandler {

    BuiltInConnectHandler(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final ServerFrame sf) {
        // Server Negotiation
        final String version = this.dataVersion(sf);
        final StompServerConnection connection = sf.connection();
        if (Objects.isNull(version)) {
            // Spec says: if the server and the client do not share any common protocol versions, then the server MUST
            // respond with an error.
            connection.write(BuiltInOut.errorVersion(connection));
            connection.close();
            return;
        }


        /*
         * Critical code logical to replace the Login / Passcode in default handler
         * That's why zero define the own handler here.
         */
        this.authenticate(sf.frame(), sf.connection(), ar -> {
            // Spec says: The server will respond back with the highest version of the protocol -> version
            connection.write(BuiltInOut.successConnected(connection, version));
        });
    }

    private void authenticate(final Frame frame, final StompServerConnection connection,
                              final Handler<AsyncResult<Void>> remainingActions) {

    }
}
