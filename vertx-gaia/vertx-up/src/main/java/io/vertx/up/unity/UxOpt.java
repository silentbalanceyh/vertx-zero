package io.vertx.up.unity;

import io.vertx.core.eventbus.DeliveryOptions;

/*
 * Unity configuration management
 * In future, all the configuration management will be here for
 * uniform calling
 */
public class UxOpt {
    /*
     * Default DeliveryOptions
     */
    public DeliveryOptions delivery() {
        final DeliveryOptions options = new DeliveryOptions();
        /* 10 min for timeout to avoid sync long works ( extend for 10 min ) */
        options.setSendTimeout(600000);
        return options;
    }
}
