package io.vertx.tp.plugin.stomp.command;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.Frame;
import io.vertx.ext.stomp.ServerFrame;
import io.vertx.ext.stomp.StompServerConnection;
import io.vertx.ext.stomp.impl.FrameParser;
import io.vertx.up.atom.secure.Aegis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractFrameHandler implements FrameWsHandler {
    protected final transient Vertx vertx;
    /*
     * Bolt for Zero Security Injection to implement custom define for STOMP
     */
    protected transient Aegis config;

    protected AbstractFrameHandler(final Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public FrameWsHandler bind(final Aegis config) {
        this.config = config;
        return this;
    }

    // Version Extract
    protected String dataVersion(final ServerFrame sf) {
        // io.vertx.ext.stomp.DefaultConnectHandler
        // void handle(ServerFrame)
        final List<String> accepted = new ArrayList<>();
        final String accept = sf.frame().getHeader(Frame.ACCEPT_VERSION);
        if (accept == null) {
            accepted.add("1.0");
        } else {
            accepted.addAll(Arrays.asList(accept.split(FrameParser.COMMA)));
        }
        // io.vertx.ext.stomp.DefaultConnectHandler
        // String negotiate(List<String> accepted, StompServerConnection connection)
        final StompServerConnection connection = sf.connection();
        final List<String> supported = connection.server().options().getSupportedVersions();
        for (final String v : supported) {
            if (accepted.contains(v)) {
                return v;
            }
        }
        return null;
    }
    // Token Extract

}
