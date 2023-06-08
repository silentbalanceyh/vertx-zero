package io.vertx.up.eon.em.container;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum RemindType {
    QUEUE,
    TOPIC,

    BRIDGE,

    /*
     * This method is configuration for `Remind` ( Related to @Subscribe ), as inner zero framework:
     *
     * - @Address means event bus address and you can publish / subscribe on this address.
     *   「FORMAT」: TOPIC://XXX/XXX
     * - @Subscribe is a new annotation for websocket, it means that you can subscribe the topic in
     *   this address instead.
     *   「FORMAT」: /job/notify or /xxx/xxxx,
     *
     *   this value will be converted to `ws://host:port/api/web-socket/stomp` with the topic = /job/notify
     *   instead of others. Here the structure should be following:
     *
     *   -- WebSocket ( /api/web-socket/stomp )
     *        Input                Topic             Message
     *     -- @Address    -->   @Subscribe   -->    <<Client>>
     *
     * Input Data Came from following Source
     *
     * 1) JavaScript Client: StompJs ( Front-End ) Directly
     *    -- SockJs         ( Non Security )
     *    -- SockBridge     ( Non Rbac )
     *    -- StompJs        ( Rbac Supported with zero-ifx-stomp )
     *    -- EventBus       ( Bridge Only )
     * 2) Back-End of RESTful Api
     *    -- EventBus       ( Bridge Only )
     *    -- Job EventBus   ( Bridge Only )
     */
    REMIND,
}
