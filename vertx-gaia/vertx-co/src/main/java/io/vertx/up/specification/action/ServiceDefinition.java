package io.vertx.up.specification.action;

import io.vertx.up.atom.exchange.DSetting;

/*
 * Service
 */
public interface ServiceDefinition extends Service {

    /*
     * `dictConfig`
     * `dictComponent` of I_SERVICE
     * `dictEpsilon` of I_SERVICE
     * Here `dictComponent` is required if configured.
     * Dictionary configuration for current Job / Component
     */
    DSetting dict();
}
