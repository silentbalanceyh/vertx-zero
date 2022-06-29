package io.vertx.up.eon.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum RemindType {
    QUEUE,
    TOPIC,

    BRIDGE,
    /*
     * New Type of Destination for Remind invoke here. the workflow is as following:
     * 1. The Notify Sender is RESTful Api/Job of zero framework.
     * 2. Finally the Api/Job will send the result to `outcomeAddress` as event bus address
     * 3. Then the event bus of subscribers will consume the address data
     * 4. Remind Part will be called in async mode and then send the `MESSAGE` command to client
     */
    REMIND,
}
