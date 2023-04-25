package io.horizon.spi.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Company Information get from system
 * You can get company information
 */
public interface ExCompany {
    /*
     * Read data by `id`
     */
    Future<JsonObject> fetchAsync(String id);
}
