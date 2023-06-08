package io.vertx.up.boot.parallel;

interface INFO {
    String SCANNED_EVENTS = "( {1} Event ) The endpoint {0} scanned {1} events of Event, " +
        "will be mounted to routing system.";

    String SCANNED_SOCKS = "( {1} WebSocket ) The endpoint {0} scanned {1} websockets of Event, " +
        "will be mounted to event bus.";

    String SCANNED_RECEIPTS = "( {1} Receipt ) The queue {0} scanned {1} records of Receipt, " +
        "will be mounted to event bus.";
    //
    //    String SCANNED_FIELD = "( Field ) Class \"{0}\" scanned field = \"{1}\" " +
    //        "of {2} annotated with {3}. will be initialized with DI container.";
    //
    //    String SCANNED_INSTANCES = "The instance classes ({0}) will be scanned.";
    //
    //    String SCANNED_JSR311 = "( Field ) JSR311 Warning, declared class: " +
    //        "\"{0}\", field = \"{1}\", type = {2}";
}
