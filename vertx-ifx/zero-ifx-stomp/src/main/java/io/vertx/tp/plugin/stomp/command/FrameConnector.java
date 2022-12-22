package io.vertx.tp.plugin.stomp.command;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.tp.plugin.stomp.socket.ServerWsHandler;
import io.vertx.up.eon.KName;
import io.vertx.up.secure.Lee;
import io.vertx.up.secure.bridge.Bolt;
import io.vertx.up.util.Ut;

import jakarta.ws.rs.core.HttpHeaders;
import java.util.Objects;

/**
 * This handler must be mount to Default because of the STOMP must require
 * zero-rbac connect instead of
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FrameConnector extends AbstractFrameHandler {

    FrameConnector(final Vertx vertx) {
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
            connection.write(FrameOutput.errorVersion(connection));
            connection.close();
            return;
        }


        /*
         * Critical code logical to replace the Login / Passcode in default handler
         * That's why zero define the own handler here.
         */
        this.authenticate(sf.frame(), sf.connection(), ar -> {
            // Spec says: The server will respond back with the highest version of the protocol -> version
            connection.write(FrameOutput.successConnected(connection, version));
        });
    }

    private void authenticate(final Frame frame, final StompServerConnection connection,
                              final Handler<AsyncResult<Void>> remainingActions) {
        if (connection.server().options().isSecured()) {
            /*
             * The Modification based on new interface to parsing the `Authorization` header
             * instead of the default web socket STOMP feature, here provider
             * {
             *     "username": "xxxx",
             *     "password": "xxxx"
             * }
             * Only, but it's not enough in zero-framework ( zero-rbac ) module, instead here should be
             * new code logical to processing the connection authorization
             *
             * Zero framework provider require following data structure based on:
             * {
             *     "access_token": "xxxx",
             *     "user": "xxxx",
             *     "habitus": "xxxx",
             *     "session": "xxxx"
             * }
             */
            final StompServerHandler handler = connection.handler();
            if (handler instanceof ServerWsHandler) {
                // Extension Code Flow
                final String authorization = frame.getHeader(HttpHeaders.AUTHORIZATION.toLowerCase());
                if (Ut.isNil(authorization)) {
                    // 401 Error
                    connection.write(FrameOutput.errorAuthenticate(connection));
                    connection.close();
                } else {
                    // Extract authorization to token
                    final JsonObject token = this.authenticateToken(authorization);
                    ((ServerWsHandler) handler).onAuthenticationRequest(connection, token, ar -> {
                        if (ar.result()) {
                            remainingActions.handle(Future.succeededFuture());
                        } else {
                            connection.write(FrameOutput.errorAuthenticate(connection));
                            connection.close();
                        }
                    });
                }
            } else {
                // Original `DefaultConnectHandler`
                this.authenticateOriginal(frame, connection, remainingActions);
            }
        } else {
            // Other action happen
            remainingActions.handle(Future.succeededFuture());
        }
    }

    private JsonObject authenticateToken(final String authorization) {
        // Extract authorization to token
        final Lee lee = Bolt.reference(this.config.getType());
        /*
         * Token String -> Json Token Object
         * 1. Token String must be split with ' ' and get the 1
         * 2. Aegis must be switched to valid value except extension
         */
        final String tokenString = authorization.split(" ")[1];
        final JsonObject token = lee.decode(tokenString, this.config.item());
        final JsonObject request = token.copy();
        request.put(KName.ACCESS_TOKEN, tokenString);
        {
            /*
             * This code is required when you want to process validated by user-defined
             * 1) Here the `token` is required by JWTAuth provider and it's connected your defined as well.
             * 2) Here are two providers merged
             *    - Standard ( JWTAuthProvider )
             *    - Extension ( @Wall annotated provider class )
             */
            request.put("token", tokenString);
        }
        return request;
    }

    private void authenticateOriginal(final Frame frame, final StompServerConnection connection,
                                      final Handler<AsyncResult<Void>> remainingActions) {
        final String login = frame.getHeader(Frame.LOGIN);
        final String passcode = frame.getHeader(Frame.PASSCODE);
        connection.handler().onAuthenticationRequest(connection, login, passcode, ar -> {
            if (ar.result()) {
                remainingActions.handle(Future.succeededFuture());
            } else {
                connection.write(FrameOutput.errorAuthenticate(connection));
                connection.close();
            }
        });
    }
}
