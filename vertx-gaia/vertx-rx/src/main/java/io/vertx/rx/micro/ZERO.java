package io.vertx.rx.micro;

interface Info {
    String RX_SERVERS = "( Rx Server ) {0} (id = {1}) Agent has deployed Rx Server on {2}.";

    String MAPPED_ROUTE = "( Uri Register ) \"{1}\" has been deployed by {0}, Options = {2}.";

    String RX_LISTEN = "( Rx Server ) {0} Rx Server has been started successfully. Endpoint: {1}.";
}
