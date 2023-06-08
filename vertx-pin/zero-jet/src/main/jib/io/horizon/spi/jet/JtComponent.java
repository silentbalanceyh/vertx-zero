package io.horizon.spi.jet;

import io.vertx.core.Future;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/*
 * Business component, connect to dao, basic condition:
 */
public interface JtComponent {
    /*
     * Access for ActIn here
     */
    Future<ActOut> transferAsync(ActIn request);
}
