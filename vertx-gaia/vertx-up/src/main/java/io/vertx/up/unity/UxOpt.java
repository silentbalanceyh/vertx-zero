package io.vertx.up.unity;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.up.runtime.deployment.DeployRotate;
import io.vertx.up.runtime.deployment.Rotate;
import io.vertx.up.uca.cache.Cc;

/*
 * Unity configuration management
 * In future, all the configuration management will be here for
 * uniform calling
 */
public class UxOpt {
    private final Cc<String, Rotate> CC_ROTATE = Cc.openThread();

    /*
     * Default DeliveryOptions
     */
    public DeliveryOptions delivery() {
        final Rotate rotate = this.CC_ROTATE.pick(DeployRotate::new); // Fn.po?lThread(this.ROTATE, DeployRotate::new);
        return rotate.spinDelivery();
    }
}
