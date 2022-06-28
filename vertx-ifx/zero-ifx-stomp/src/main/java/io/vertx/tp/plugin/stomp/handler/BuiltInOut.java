package io.vertx.tp.plugin.stomp.handler;

import io.vertx.ext.stomp.Command;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.Frames;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.ext.stomp.utils.Server;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BuiltInOut {

    static Frame successConnected(final StompServerConnection connection, final String version) {
        return new Frame(Command.CONNECTED, Headers.create(
            Frame.VERSION, version,
            Frame.SERVER, Server.SERVER_NAME,
            Frame.SESSION, connection.session(),
            Frame.HEARTBEAT, Frame.Heartbeat.create(
                connection.server().options().getHeartbeat()).toString()
        ), null);
    }

    static Frame errorVersion(final StompServerConnection connection) {
        return Frames.createErrorFrame(
            "[ Zero ] Incompatible versions",
            Headers.create(
                Frame.VERSION, getSupportedVersionsHeaderLine(connection),
                Frame.CONTENT_TYPE, "text/plain"),
            "Client protocol requirement does not mach versions supported by the server. " +
                "Supported protocol versions are " + getSupportedVersionsHeaderLine(connection));
    }

    // ---------------------- Private Method ----------------------
    private static String getSupportedVersionsHeaderLine(final StompServerConnection connection) {
        final StringBuilder builder = new StringBuilder();
        connection.server().options().getSupportedVersions().forEach(
            v -> builder.append(builder.length() == 0 ? v : FrameParser.COMMA + v));
        return builder.toString();
    }
}
