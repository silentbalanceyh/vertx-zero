package io.vertx.tp.plugin.stomp.socket;

import io.vertx.core.*;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.future.PromiseInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.impl.*;
import io.vertx.up.log.Annal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ServerStompHandler implements ServerWsHandler {

    private static final Annal LOGGER = Annal.get(ServerStompHandler.class);
    private final Vertx vertx;
    private final Context context;
    private final LocalMap<Destination, String> destinations;
    // user is mutable and built from other modules so there's no guarantees
    // about thread safety so use w/ care..
    private final ConcurrentHashMap<String, User> users;
    private Handler<ServerFrame> connectHandler = new DefaultConnectHandler();
    private Handler<ServerFrame> stompHandler;
    private Handler<ServerFrame> sendHandler = new DefaultSendHandler();
    private Handler<ServerFrame> subscribeHandler = new DefaultSubscribeHandler();
    private Handler<ServerFrame> unsubscribeHandler = new DefaultUnsubscribeHandler();
    private Handler<StompServerConnection> closeHandler;
    private Handler<ServerFrame> commitHandler = new DefaultCommitHandler();
    private Handler<ServerFrame> abortHandler = new DefaultAbortHandler();
    private Handler<ServerFrame> beginHandler = new DefaultBeginHandler();
    private Handler<ServerFrame> ackHandler = new DefaultAckHandler();
    private Handler<ServerFrame> nackHandler = new DefaultNackHandler();
    private Handler<ServerFrame> disconnectHandler = (sf -> {
        StompServerConnection connection = sf.connection();
        Frames.handleReceipt(sf.frame(), connection);
        connection.close();
    });
    private AuthenticationProvider authProvider;
    private Handler<StompServerConnection> pingHandler = StompServerConnection::ping;
    private Handler<Acknowledgement> onAckHandler = (acknowledgement) -> LOGGER.info("Acknowledge messages - " +
        acknowledgement.frames());
    private Handler<Acknowledgement> onNackHandler = (acknowledgement) ->
        LOGGER.warn("Messages not acknowledge - " + acknowledgement.frames());
    private DestinationFactory factory = Destination::topic;

    private Handler<ServerFrame> receivedFrameHandler;

    /**
     * Creates a new instance of {@link DefaultStompHandler}.
     *
     * @param vertx the vert.x instance
     */
    public ServerStompHandler(final Vertx vertx) {
        this.vertx = vertx;
        this.context = Vertx.currentContext();
        this.destinations = vertx.sharedData().getLocalMap("stomp.destinations");
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void onClose(final StompServerConnection connection) {
        // Default behavior.
        this.getDestinations().forEach((d) -> d.unsubscribeConnection(connection));
        Transactions.instance().unregisterTransactionsFromConnection(connection);

        // Remove user, if exists
        this.users.remove(connection.session());

        if (this.closeHandler != null) {
            this.closeHandler.handle(connection);
        }
    }

    @Override
    public synchronized StompServerHandler receivedFrameHandler(final Handler<ServerFrame> handler) {
        this.receivedFrameHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler connectHandler(final Handler<ServerFrame> handler) {
        this.connectHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler stompHandler(final Handler<ServerFrame> handler) {
        this.stompHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler subscribeHandler(final Handler<ServerFrame> handler) {
        this.subscribeHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler unsubscribeHandler(final Handler<ServerFrame> handler) {
        this.unsubscribeHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler sendHandler(final Handler<ServerFrame> handler) {
        this.sendHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler closeHandler(final Handler<StompServerConnection> handler) {
        this.closeHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler commitHandler(final Handler<ServerFrame> handler) {
        this.commitHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler abortHandler(final Handler<ServerFrame> handler) {
        this.abortHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler beginHandler(final Handler<ServerFrame> handler) {
        this.beginHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler disconnectHandler(final Handler<ServerFrame> handler) {
        this.disconnectHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler ackHandler(final Handler<ServerFrame> handler) {
        this.ackHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler nackHandler(final Handler<ServerFrame> handler) {
        this.nackHandler = handler;
        return this;
    }

    @Override
    public void handle(final ServerFrame serverFrame) {
        final Frame frame = serverFrame.frame();
        final StompServerConnection connection = serverFrame.connection();
        connection.onServerActivity();

        synchronized (this) {
            if (this.receivedFrameHandler != null) {
                this.receivedFrameHandler.handle(serverFrame);
            }
        }

        switch (frame.getCommand()) {
            case CONNECT:
                this.handleConnect(frame, connection);
                break;
            case STOMP:
                this.handleStomp(frame, connection);
                break;
            case SEND:
                this.handleSend(frame, connection);
                break;
            case SUBSCRIBE:
                this.handleSubscribe(frame, connection);
                break;
            case UNSUBSCRIBE:
                this.handleUnsubscribe(frame, connection);
                break;
            case BEGIN:
                this.handleBegin(frame, connection);
                break;
            case ABORT:
                this.handleAbort(frame, connection);
                break;
            case COMMIT:
                this.handleCommit(frame, connection);
                break;
            case ACK:
                this.handleAck(frame, connection);
                break;
            case NACK:
                this.handleNack(frame, connection);
                break;
            case DISCONNECT:
                this.handleDisconnect(frame, connection);
                break;
            case PING:
                // We received a ping frame, we do nothing.
                break;
            default:
                // Unknown frames
                break;
        }
    }

    private void handleAck(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.ackHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleNack(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.nackHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleBegin(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.beginHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleAbort(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.abortHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleCommit(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.commitHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleSubscribe(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.subscribeHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleUnsubscribe(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.unsubscribeHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleSend(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.sendHandler;
        }

        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleConnect(final Frame frame, final StompServerConnection connection) {

        final Handler<ServerFrame> handler;
        final Handler<StompServerConnection> pingH;
        synchronized (this) {
            handler = this.connectHandler;
            pingH = this.pingHandler;
        }

        // Compute heartbeat, and register pinger and ponger
        // Stomp server acts as a client to call the computePingPeriod & computePongPeriod method
        final long ping = Frame.Heartbeat.computePingPeriod(
            Frame.Heartbeat.create(connection.server().options().getHeartbeat()),
            Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));
        final long pong = Frame.Heartbeat.computePongPeriod(
            Frame.Heartbeat.create(connection.server().options().getHeartbeat()),
            Frame.Heartbeat.parse(frame.getHeader(Frame.HEARTBEAT)));

        connection.configureHeartbeat(ping, pong, pingH);

        // Then, handle the frame.
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleDisconnect(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.disconnectHandler;
        }
        if (handler != null) {
            handler.handle(new ServerFrameImpl(frame, connection));
        }
    }

    private void handleStomp(final Frame frame, final StompServerConnection connection) {
        final Handler<ServerFrame> handler;
        synchronized (this) {
            handler = this.stompHandler;
        }
        if (handler == null) {
            // Per spec, STOMP frame must be handled as CONNECT
            this.handleConnect(frame, connection);
            return;
        }
        handler.handle(new ServerFrameImpl(frame, connection));
    }

    @Override
    public synchronized StompServerHandler authProvider(final AuthenticationProvider handler) {
        this.authProvider = handler;
        return this;
    }

    @Override
    public Future<Boolean> onAuthenticationRequest(final StompServerConnection connection, final String login, final String passcode) {
        final PromiseInternal<Boolean> promise = ((ContextInternal) this.context).promise();
        this.onAuthenticationRequest(connection, login, passcode, promise);
        return promise.future();
    }

    @Override
    public StompServerHandler onAuthenticationRequest(final StompServerConnection connection,
                                                      final String login, final String passcode,
                                                      final Handler<AsyncResult<Boolean>> handler) {
        final AuthenticationProvider auth;
        synchronized (this) {
            // Stack contention.
            auth = this.authProvider;
        }

        final StompServer server = connection.server();
        if (!server.options().isSecured()) {
            if (auth != null) {
                LOGGER.warn("Authentication handler set while the server is not secured");
            }
            this.context.runOnContext(v -> handler.handle(Future.succeededFuture(true)));
            return this;
        }

        if (server.options().isSecured() && auth == null) {
            LOGGER.error("Cannot authenticate connection - no authentication provider");
            this.context.runOnContext(v -> handler.handle(Future.succeededFuture(false)));
            return this;
        }


        this.context.runOnContext(v -> auth.authenticate(
            new JsonObject().put("username", login).put("password", passcode),
            this.onAuthenticationOut(connection, handler))
        );
        return this;
    }

    /**
     * Return the authenticated user for this session.
     *
     * @param session session ID for the server connection.
     *
     * @return the user provided by the {@link AuthenticationProvider} or null if not found.
     */
    @Override
    public User getUserBySession(final String session) {
        return this.users.get(session);
    }

    @Override
    public List<Destination> getDestinations() {
        return new ArrayList<>(this.destinations.keySet());
    }

    /**
     * Gets the destination with the given name..
     *
     * @param destination the destination
     *
     * @return the {@link Destination}, {@code null} if not found.
     */
    @Override
    public Destination getDestination(final String destination) {
        for (final Destination d : this.destinations.keySet()) {
            if (d.matches(destination)) {
                return d;
            }
        }
        return null;
    }

    @Override
    public Destination getOrCreateDestination(final String destination) {
        final DestinationFactory destinationFactory;
        synchronized (this) {
            destinationFactory = this.factory;
        }
        synchronized (this.vertx) {
            Destination d = this.getDestination(destination);
            if (d == null) {
                d = destinationFactory.create(this.vertx, destination);
                if (d != null) {
                    // We use the local map as a set, the value is irrelevant.
                    this.destinations.put(d, "");
                }
            }
            return d;
        }
    }

    @Override
    public synchronized StompServerHandler destinationFactory(final DestinationFactory factory) {
        this.factory = factory;
        return this;
    }

    /**
     * Configures the STOMP server to act as a bridge with the Vert.x event bus.
     *
     * @param options the configuration options
     *
     * @return the current {@link StompServerHandler}.
     * @see Vertx#eventBus()
     */
    @Override
    public synchronized StompServerHandler bridge(final BridgeOptions options) {

        final Destination remindDestination = new RemindDestination(this.vertx);
        this.destinations.put(remindDestination, "");
        // Old Code Removed
        // this.destinations.put(Destination.bridge(this.vertx, options), "");
        return this;
    }

    @Override
    public StompServerHandler onAck(final StompServerConnection connection, final Frame subscription, final List<Frame> messages) {
        final Handler<Acknowledgement> handler;
        synchronized (this) {
            handler = this.onAckHandler;
        }
        if (handler != null) {
            handler.handle(new AcknowledgementImpl(subscription, messages));
        }
        return this;
    }

    @Override
    public StompServerHandler onNack(final StompServerConnection connection, final Frame subscribe, final List<Frame> messages) {
        final Handler<Acknowledgement> handler;
        synchronized (this) {
            handler = this.onNackHandler;
        }
        if (handler != null) {
            handler.handle(new AcknowledgementImpl(subscribe, messages));
        }
        return this;
    }

    @Override
    public synchronized StompServerHandler onAckHandler(final Handler<Acknowledgement> handler) {
        this.onAckHandler = handler;
        return this;
    }

    @Override
    public synchronized StompServerHandler onNackHandler(final Handler<Acknowledgement> handler) {
        this.onNackHandler = handler;
        return this;
    }

    /**
     * Allows customizing the action to do when the server needs to send a `PING` to the client. By default it send a
     * frame containing {@code EOL} (specification). However, you can customize this and send another frame. However,
     * be aware that this may requires a custom client.
     * <p/>
     * The handler will only be called if the connection supports heartbeats.
     *
     * @param handler the action to execute when a `PING` needs to be sent.
     *
     * @return the current {@link StompServerHandler}
     */
    @Override
    public synchronized StompServerHandler pingHandler(final Handler<StompServerConnection> handler) {
        this.pingHandler = handler;
        return this;
    }

    @Override
    public StompServerHandler onAuthenticationRequest(final StompServerConnection connection,
                                                      final JsonObject principal,
                                                      final Handler<AsyncResult<Boolean>> handler) {

        /* secured = false, the provider is not null        ( Warning ),    return : true. */
        final StompServer server = connection.server();
        if (!server.options().isSecured()) {
            if (Objects.nonNull(this.authProvider)) {
                LOGGER.warn("Authentication handler set while the server is not secured");
            }
            this.context.runOnContext(v -> handler.handle(Future.succeededFuture(true)));
        }


        /* secured = true, the provider must not be null    ( Error ),      return : false. */
        if (server.options().isSecured() && Objects.isNull(this.authProvider)) {
            LOGGER.error("Cannot authenticate connection - no authentication provider");
            this.context.runOnContext(v -> handler.handle(Future.succeededFuture(false)));
        }

        this.context.runOnContext(v -> this.authProvider.authenticate(
            principal,
            this.onAuthenticationOut(connection, handler)
        ));
        return this;
    }

    // ---------------------- Extension Code ------------------------

    private Handler<AsyncResult<User>> onAuthenticationOut(final StompServerConnection connection,
                                                           final Handler<AsyncResult<Boolean>> handler) {
        return ar -> {
            if (ar.succeeded()) {
                // Make the user available here
                this.users.put(connection.session(), ar.result());
                this.context.runOnContext(v2 -> handler.handle(Future.succeededFuture(true)));
            } else {
                this.context.runOnContext(v2 -> handler.handle(Future.succeededFuture(false)));
            }
        };
    }
}
