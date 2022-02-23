package io.vertx.tp.optic.environment;

import io.vertx.core.MultiMap;
import io.vertx.tp.plugin.database.DataPool;

/*
 * Data Source switching channel
 * For dynamic data source capture here instead of
 * Only one
 */
public interface DS {
    /*
     * Sync method to get `Data Source`
     */
    DataPool switchDs(MultiMap headers);

    DataPool switchDs(String sigma);
}
