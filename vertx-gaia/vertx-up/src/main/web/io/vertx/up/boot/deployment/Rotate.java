package io.vertx.up.boot.deployment;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.DeliveryOptions;

/*
 * Deployment options for agent and worker
 * 1) For defined options, you can set all the options by @Agent/@Worker
 * 2) For dynamic, you must configured the options in `vertx-`
 */
public interface Rotate {
    /*
     * Get agent options
     */
    DeploymentOptions spinAgent(Class<?> clazz);

    /*
     * Get worker options
     */
    DeploymentOptions spinWorker(Class<?> clazz);

    /*
     * Delivery options
     */
    DeliveryOptions spinDelivery();
}
