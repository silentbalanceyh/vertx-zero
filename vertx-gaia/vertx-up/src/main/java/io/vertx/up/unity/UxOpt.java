package io.vertx.up.unity;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.deployment.DeployRotate;
import io.vertx.up.runtime.deployment.Rotate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Unity configuration management
 * In future, all the configuration management will be here for
 * uniform calling
 */
public class UxOpt {
    private final ConcurrentMap<String, Rotate> ROTATE = new ConcurrentHashMap<>();

    /*
     * Default DeliveryOptions
     */
    public DeliveryOptions delivery() {
        final Rotate rotate = Fn.poolThread(this.ROTATE, DeployRotate::new);
        return rotate.spinDelivery();
    }
}
