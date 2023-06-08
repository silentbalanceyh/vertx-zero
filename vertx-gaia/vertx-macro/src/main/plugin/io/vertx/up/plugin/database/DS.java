package io.vertx.up.plugin.database;

import io.vertx.core.MultiMap;

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
