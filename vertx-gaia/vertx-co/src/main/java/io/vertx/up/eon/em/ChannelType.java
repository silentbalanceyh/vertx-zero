package io.vertx.up.eon.em;

/*
 * Api -> Channel -> Service
 * Here are many kind of channels and it's used in different business requirements
 */
public enum ChannelType {
    /*
     * 「Adaptor」
     *  1）This channel is for X_SOURCE, this component could be cross different database such as
     *     MySQL, Oracle, etc.
     *  2）In this kind of situation, the component major features are "SQL Database Adapting", that's why it's name
     *     is Adaptor.
     *  3) If you involve X_SOURCE, one app could access different database, otherwise, you could use `vertx-jooq.yml`
     *     configuration. When you want to get `DSLContext` reference, you could access it with
     *     `ZPool.create(Database)` method here.
     */
    ADAPTOR,
    /*
     * 「Connector」
     *  1）This channel is for I_API/I_SERVICE, the component should connect different third-part system with
     *     integration components.
     *  2）In this kind of situation, the component just like a "Uniform Connector" to any kind of third-part
     *     system, that's why the name is Connector
     */
    CONNECTOR,
    /*
     * 「Actor」
     *  1）This name came from `Re-Actor` design pattern, it's async background tasks. In zero system ( vert.x ), this
     *     component could be implemented with Worker only.
     *  2）Must be:
     *    2.1）The server should be active and send message to another position here.
     *    2.2）There are two mode: Once / Scheduled / Triggered
     */
    ACTOR,
    /*
     * 「Director」
     *  1）In default situation, the api is bind to `identifier` of model, but this kind of channel may be combine more than
     *     One model such as relations here.
     *  2）You can access different Dao async in this channel and connect to different `Component` here.
     */
    DIRECTOR,

    DEFINE,
}
