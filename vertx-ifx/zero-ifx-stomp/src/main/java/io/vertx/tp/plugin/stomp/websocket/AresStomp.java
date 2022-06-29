package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServer;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.tp.plugin.stomp.command.FrameWsHandler;
import io.vertx.tp.plugin.stomp.socket.ServerWsHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.em.RemindType;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.secure.bridge.Bolt;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.matcher.RegexPath;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroAtomic;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresStomp extends AbstractAres {
    private static final Cc<String, Set<Aegis>> CC_WALLS = ZeroAnno.getWalls();
    private static final AtomicBoolean LOG_FOUND = new AtomicBoolean(Boolean.TRUE);
    private static final AtomicBoolean LOG_PROVIDER = new AtomicBoolean(Boolean.TRUE);
    private transient final Bolt bolt;
    private final Set<Remind> sockOk;


    public AresStomp(final Vertx vertx) {
        super(vertx);
        this.bolt = Bolt.get();
        this.sockOk = this.socks(true);
    }

    @Override
    public void configure(final HttpServerOptions options) {
        super.configure(options);
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.get(options.getPort());
        Objects.requireNonNull(sockOptions);
        final HttpServerOptions configured = sockOptions.options();
        if (Objects.nonNull(configured)) {
            /* Re-define for WebSocket 8 attributes */
            options.setWebSocketAllowServerNoContext(configured.getWebSocketAllowServerNoContext());
            options.setWebSocketClosingTimeout(configured.getWebSocketClosingTimeout());
            options.setWebSocketCompressionLevel(configured.getWebSocketCompressionLevel());
            options.setWebSocketPreferredClientNoContext(configured.getWebSocketPreferredClientNoContext());
            /* Here must include stomp sub protocols */
            options.setWebSocketSubProtocols(configured.getWebSocketSubProtocols());

            options.setMaxWebSocketFrameSize(configured.getMaxWebSocketFrameSize());
            options.setMaxWebSocketMessageSize(configured.getMaxWebSocketMessageSize());
            options.setPerFrameWebSocketCompressionSupported(configured.getPerFrameWebSocketCompressionSupported());
            options.setPerMessageWebSocketCompressionSupported(configured.getPerMessageWebSocketCompressionSupported());
        }
    }

    @Override
    public void mount(final Router router, final JsonObject config) {
        final SockOptions sockOptions = ZeroAtomic.SOCK_OPTS.get(this.options.getPort());
        Objects.requireNonNull(sockOptions);
        final JsonObject stompJ = Ut.valueJObject(sockOptions.getConfig(), "stomp");
        final StompServerOptions stompOptions = new StompServerOptions(stompJ);
        final StompServer stompServer = StompServer.create(this.vertx(), stompOptions);
        // Iterator the SOCKS
        final ServerWsHandler handler = ServerWsHandler.create(this.vertx());

        // Security for WebSocket
        final Aegis aegis = this.mountAuthenticateProvider(handler, stompOptions);

        // Mount user definition handler
        this.mountHandler(handler, aegis);

        // Mount destination
        this.mountDestination(handler);

        // Build StompServer and bind webSocketHandler
        stompServer.handler(handler);
        this.server.webSocketHandler(stompServer.webSocketHandler());
    }

    private void mountDestination(final StompServerHandler handler) {
        /*
         * Build Map of address = type
         * Here are two types
         */
        final ConcurrentMap<String, RemindType> topicMap = new ConcurrentHashMap<>();
        this.sockOk.forEach(remind -> {
            final String subscribe = remind.getSubscribe();
            if (Ut.notNil(subscribe)) {
                topicMap.put(subscribe, Objects.isNull(remind.getType()) ? RemindType.TOPIC : remind.getType());
            }
        });
        // Destination Building
        handler.destinationFactory((v, name) -> {
            final RemindType type = topicMap.getOrDefault(name, null);
            if (Objects.isNull(type)) {
                // No Definition of Address
                this.logger().info(Info.SUBSCRIBE_NULL, name);
                return null;
            }
            if (RemindType.QUEUE == type) {
                // Queue
                this.logger().info(Info.SUBSCRIBE_QUEUE, name);
                return Destination.queue(v, name);
            } else {
                // Topic
                this.logger().info(Info.SUBSCRIBE_TOPIC, name);
                return Destination.topic(v, name);
            }
        });
    }

    private void mountHandler(final StompServerHandler handler, final Aegis aegis) {
        // Replace Connect Handler because of Security Needed.
        final FrameWsHandler connectHandler = FrameWsHandler.connector(this.vertx());
        handler.connectHandler(connectHandler.bind(aegis));
    }

    private Aegis mountAuthenticateProvider(final StompServerHandler handler, final StompServerOptions option) {
        // Stomp Path Find
        final String stomp = option.getWebsocketPath();
        final AtomicReference<Aegis> reference = new AtomicReference<>();
        CC_WALLS.store().data().forEach((path, aegisSet) -> {
            /*
             * Stomp:   /api/web-socket/stomp
             * Path:    /api/
             */
            final Pattern regexPath = RegexPath.createRegex(path);
            if (!aegisSet.isEmpty() && regexPath.matcher(stomp).matches()) {
                if (LOG_FOUND.getAndSet(Boolean.FALSE)) {
                    this.logger().info(Info.SECURE_FOUND, stomp, path, String.valueOf(aegisSet.size()));
                }
                reference.set(aegisSet.iterator().next());
            }
        });
        final Aegis config = reference.get();
        if (Objects.nonNull(config)) {
            final AuthenticationProvider provider = this.bolt.authenticateProvider(this.vertx(), config);
            if (LOG_PROVIDER.getAndSet(Boolean.FALSE)) {
                this.logger().info(Info.SECURE_PROVIDER, provider.getClass());
            }
            handler.authProvider(provider);
        }
        return config;
    }
}
