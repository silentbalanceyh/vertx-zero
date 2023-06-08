package io.vertx.up.plugin.stomp.websocket;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Info {

    String SECURE_FOUND = "( AresStomp ) Zero has found Aegis item: ( stomp = {0}, path = {1}, size = {2} )";

    String SECURE_PROVIDER = "( AresStomp ) Zero will use security provider: {0}";

    String SUBSCRIBE_REMIND = "( AresStomp ) The Remind of `{0}` has been subscribed.";
    String SUBSCRIBE_QUEUE = "( AresStomp ) The Queue of `{0}` has been subscribed.";
    String SUBSCRIBE_BRIDGE = "( AresStomp ) The Bridge of `{0}` has been subscribed.";
    String SUBSCRIBE_TOPIC = "( AresStomp ) The Topic of `{0}` has been subscribed.";
    String SUBSCRIBE_NULL = "( AresStomp ) The address `{0}` is invalid and could not be subscribed.";
}
