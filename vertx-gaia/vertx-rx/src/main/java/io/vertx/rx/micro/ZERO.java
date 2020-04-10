package io.vertx.rx.micro;

import io.vertx.reactivex.ext.web.Router;
import io.vertx.up.uca.rs.Axis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, Axis<Router>> ROUTERS = new ConcurrentHashMap<>();

    ConcurrentMap<String, Axis<Router>> EVENTS = new ConcurrentHashMap<>();
}

interface Info {
    String RX_SERVERS = "( Rx Server ) {0} (id = {1}) Agent has deployed Rx Server on {2}.";

    String MAPPED_ROUTE = "( Uri Register ) \"{1}\" has been deployed by {0}, Options = {2}.";

    String RX_LISTEN = "( Rx Server ) {0} Rx Server has been started successfully. Endpoint: {1}.";
}
