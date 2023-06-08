package io.horizon.spi.jet;

import io.vertx.core.Future;
import io.vertx.up.commune.Envelop;
import io.vertx.up.specification.action.Commercial;

/*
 * Consumer interface
 * Worker and Consumer definition are all stored in `I_API` and `I_SERVICE`, You can use one worker with more
 * Consumer/Address bind, it means that each worker could contain more than one Consumer/Address here.
 * The design consideration
 * 1. The api code logical will hit the Worker first, the worker instances will be set to related to threads
 * 2. Worker existing is for performance improvement to loading balance, Once the worker is more overload, you can
 * expand another.
 * 3. The Address / Consumer is the bind ( 1 - 1 ) here, but the Worker are not.
 */
public interface JtConsumer {
    /*
     * Consumer will consume the data from address
     */
    Future<Envelop> async(Envelop envelop, Commercial commercial);
}
