package io.vertx.up.eon.em.container;

/*
 * Here are two positions that will be used
 * 1) @Worker annotation, it's used by @Worker component
 * 2) Origin X Engine, stored into `I_API` table of database.
 *
 * In current version, not all below values are supported by zero.
 */
public enum MessageModel {
    /*
     * Common Http Worker here
     * Request -> Response
     */
    REQUEST_RESPONSE,                   // Origin X Available ( Stored )
    REQUEST_MICRO_WORKER,

    /*
     * Common Publisher
     * Publish -> Subscribe
     */
    PUBLISH_SUBSCRIBE,                  // Origin X Available ( Stored )

    /*
     * Micro Discovery Publisher
     * Discovery -> Publish
     */
    DISCOVERY_PUBLISH,

    /*
     * Common Http Worker here
     * Request -> ( Background Workers without response )
     */
    ONE_WAY                             // Origin X Available ( Stored )
}
