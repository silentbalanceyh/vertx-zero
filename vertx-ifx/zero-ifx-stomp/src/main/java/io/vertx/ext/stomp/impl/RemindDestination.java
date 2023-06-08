package io.vertx.ext.stomp.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.*;
import io.vertx.ext.stomp.utils.Headers;
import io.vertx.up.extension.router.AresGrid;
import io.vertx.up.plugin.stomp.websocket.BridgeStomp;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copy from EventBusBridge, here we'll modify some code logical and involve
 * the @Subscribe proxy method instead, in this kind of situation the default
 * operation will be modified
 *
 * I'm not sure whether the default implementation is a bug because the
 * InBound, OutBound configure have issues.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RemindDestination extends Topic {

    private final BridgeOptions options;

    private final Map<String, Pattern> expressions = new HashMap<>();

    private final Map<String, MessageConsumer<?>> registry = new HashMap<>();


    public RemindDestination(final Vertx vertx) {
        super(vertx, null);
        this.options = BridgeStomp.wsOptionBridge();
    }

    @Override
    public String destination() {
        return "<<bridge>>";
    }

    @Override
    public synchronized Destination subscribe(final StompServerConnection connection, final Frame frame) {
        final String address = frame.getDestination();
        /*
         * Need to check whether the client can receive message from the event bus (outbound).
         * Here the destination address is subscribe address
         * 1) InBound = @Address to EventBus
         * 2) OutBound = @Subscribe to WebSocket
         *
         * Here we'll split the address of Subscribe & EventBus to implement user-defined
         * @Subscribe("xxxx") @Address("yyyy")
         */
        String addressEvent = AresGrid.configAddress(address);
        if (Ut.isNil(addressEvent)) {
            // The Map dose not store the address
            addressEvent = address;
        }
        if (this.checkMatches(false, address, null)) {
            // We need the subscription object to transform messages.
            final Subscription subscription = new Subscription(connection, frame);
            this.subscriptions.add(subscription);
            if (!this.registry.containsKey(address)) {
                this.registry.put(address, this.vertx.eventBus().consumer(addressEvent, msg -> {
                    if (!this.checkMatches(false, address, msg.body())) {
                        return;
                    }

                    /* Code modified here for inject method calling */
                    AresGrid.wsInvoke(address, msg.body(), (returned) -> {
                        if (this.options.isPointToPoint()) {
                            final Optional<Subscription> chosen = this.subscriptions.stream().filter(s -> s.destination.equals(address)).findAny();
                            if (chosen.isPresent()) {
                                final Frame stompFrame = this.transform(msg, chosen.get(), returned.result());
                                chosen.get().connection.write(stompFrame);
                            }
                        } else {
                            this.subscriptions.stream().filter(s -> s.destination.equals(address)).forEach(s -> {
                                final Frame stompFrame = this.transform(msg, s, returned.result());
                                s.connection.write(stompFrame);
                            });
                        }
                    });
                }));
            }
            return this;
        }
        return null;
    }

    @Override
    public synchronized boolean unsubscribe(final StompServerConnection connection, final Frame frame) {
        for (final Subscription subscription : new ArrayList<>(this.subscriptions)) {
            if (subscription.connection.equals(connection)
                && subscription.id.equals(frame.getId())) {

                final boolean r = this.subscriptions.remove(subscription);
                this.unsubscribe(subscription);
                return r;
            }
        }
        return false;
    }

    @Override
    public synchronized Destination unsubscribeConnection(final StompServerConnection connection) {
        new ArrayList<>(this.subscriptions)
            .stream()
            .filter(subscription -> subscription.connection.equals(connection))
            .forEach(s -> {
                this.subscriptions.remove(s);
                this.unsubscribe(s);
            });
        return this;
    }

    private void unsubscribe(final Subscription s) {
        final Optional<Subscription> any = this.subscriptions.stream().filter(s2 -> s2.destination.equals(s.destination))
            .findAny();
        // We unregister the event bus consumer if there are no subscription on this address anymore.
        if (any.isEmpty()) {
            final MessageConsumer<?> consumer = this.registry.remove(s.destination);
            if (consumer != null) {
                consumer.unregister();
            }
        }
    }

    private Frame transform(final Message<Object> msg, final Subscription subscription, final Object bodyData) {
        final String messageId = UUID.randomUUID().toString();

        final Frame frame = new Frame();
        frame.setCommand(Command.MESSAGE);

        final Headers headers = Headers.create(frame.getHeaders())
            // Destination already set in the input headers.
            .add(Frame.SUBSCRIPTION, subscription.id)
            .add(Frame.MESSAGE_ID, messageId)
            .add(Frame.DESTINATION, msg.address());
        if (!"auto".equals(subscription.ackMode)) {
            // We reuse the message Id as ack Id
            headers.add(Frame.ACK, messageId);
        }

        // Specific headers.
        if (msg.replyAddress() != null) {
            headers.put("reply-address", msg.replyAddress());
        }

        // Copy headers.
        for (final Map.Entry<String, String> entry : msg.headers()) {
            headers.putIfAbsent(entry.getKey(), entry.getValue());
        }

        frame.setHeaders(headers);

        final Object body = Objects.nonNull(bodyData) ? bodyData : msg.body();
        if (body != null) {
            if (body instanceof String) {
                frame.setBody(Buffer.buffer((String) body));
            } else if (body instanceof Buffer) {
                frame.setBody((Buffer) body);
            } else if (body instanceof JsonObject) {
                frame.setBody(Buffer.buffer(((JsonObject) body).encode()));
            } else {
                throw new IllegalStateException("Illegal body - unsupported body type: " + body.getClass().getName());
            }
        }

        if (body != null && frame.getHeader(Frame.CONTENT_LENGTH) == null) {
            frame.addHeader(Frame.CONTENT_LENGTH, Integer.toString(frame.getBody().length()));
        }

        return frame;
    }

    @Override
    public Destination dispatch(final StompServerConnection connection, final Frame frame) {
        final String address = frame.getDestination();
        // Send a frame to the event bus, check if this inbound traffic is allowed.
        if (this.checkMatches(true, address, frame.getBody())) {
            final String replyAddress = frame.getHeader("reply-address");
            if (replyAddress != null) {
                this.send(address, frame, (final AsyncResult<Message<Object>> res) -> {
                    if (res.failed()) {
                        final Throwable cause = res.cause();
                        connection.write(Frames.createErrorFrame("Message dispatch error", Headers.create(Frame.DESTINATION,
                            address, "reply-address", replyAddress), cause.getMessage())).close();
                    } else {
                        // We are in a request-response interaction, only one STOMP client must receive the message (the one
                        // having sent the given frame).
                        // We look for the subscription with registered to the 'reply-to' destination. It must be unique.
                        final Optional<Subscription> subscription = this.subscriptions.stream()
                            .filter(s -> s.connection.equals(connection) && s.destination.equals(replyAddress))
                            .findFirst();
                        subscription.ifPresent(value -> AresGrid.wsInvoke(address, res.result(), (returned) -> {
                            final Frame stompFrame = this.transform(res.result(), value, returned.result());
                            value.connection.write(stompFrame);
                        }));
                    }
                });
            } else {
                this.send(address, frame, null);
            }
        } else {
            connection.write(Frames.createErrorFrame("Access denied", Headers.create(Frame.DESTINATION,
                address), "Access denied to " + address)).close();
            return null;
        }
        return this;
    }

    private void send(final String address, final Frame frame, final Handler<AsyncResult<Message<Object>>> replyHandler) {
        // Event Bus Address seeking
        String addressEvent = AresGrid.configAddress(address);
        if (Ut.isNil(addressEvent)) {
            // The Map dose not store the address
            addressEvent = address;
        }
        if (this.options.isPointToPoint()) {
            this.vertx.eventBus().request(addressEvent, frame.getBody(),
                new DeliveryOptions().setHeaders(this.toMultimap(frame.getHeaders())), replyHandler);
        } else {
            // the reply handler is ignored in non point to point interaction.
            this.vertx.eventBus().publish(addressEvent, frame.getBody(),
                new DeliveryOptions().setHeaders(this.toMultimap(frame.getHeaders())));
        }
    }

    private MultiMap toMultimap(final Map<String, String> headers) {
        return MultiMap.caseInsensitiveMultiMap().addAll(headers);
    }

    @Override
    public boolean matches(final String address) {
        return this.checkMatches(false, address, null) || this.checkMatches(true, address, null);
    }

    private boolean regexMatches(final String matchRegex, final String address) {
        Pattern pattern = this.expressions.get(matchRegex);
        if (pattern == null) {
            pattern = Pattern.compile(matchRegex);
            this.expressions.put(matchRegex, pattern);
        }
        final Matcher m = pattern.matcher(address);
        return m.matches();
    }

    private boolean checkMatches(final boolean inbound, final String address, final Object body) {

        final List<PermittedOptions> matches = inbound ? this.options.getInboundPermitteds() : this.options.getOutboundPermitteds();

        for (final PermittedOptions matchHolder : matches) {
            final String matchAddress = matchHolder.getAddress();
            final String matchRegex;
            if (matchAddress == null) {
                matchRegex = matchHolder.getAddressRegex();
            } else {
                matchRegex = null;
            }

            final boolean addressOK;
            if (matchAddress == null) {
                addressOK = matchRegex == null || this.regexMatches(matchRegex, address);
            } else {
                addressOK = matchAddress.equals(address);
            }

            if (addressOK) {
                return this.structureMatches(matchHolder.getMatch(), body);
            }
        }

        return false;
    }

    private boolean structureMatches(final JsonObject match, final Object body) {
        if (match == null || body == null) {
            return true;
        }

        // Can send message other than JSON too - in which case we can't do deep matching on structure of message

        try {
            final JsonObject object;
            if (body instanceof JsonObject) {
                object = (JsonObject) body;
            } else if (body instanceof Buffer) {
                object = new JsonObject(((Buffer) body).toString("UTF-8"));
            } else if (body instanceof String) {
                object = new JsonObject((String) body);
            } else {
                return false;
            }

            for (final String fieldName : match.fieldNames()) {
                final Object mv = match.getValue(fieldName);
                final Object bv = object.getValue(fieldName);
                // Support deep matching
                if (mv instanceof JsonObject) {
                    if (!this.structureMatches((JsonObject) mv, bv)) {
                        return false;
                    }
                } else if (!match.getValue(fieldName).equals(object.getValue(fieldName))) {
                    return false;
                }
            }
            return true;
        } catch (final Exception e) {
            // Was not a valid json object refuse the message
            return false;
        }
    }
}
