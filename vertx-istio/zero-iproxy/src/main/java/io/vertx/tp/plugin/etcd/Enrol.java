package io.vertx.tp.plugin.etcd;

import io.vertx.core.json.JsonObject;

/**
 * Register Center to connect to etcd
 */
public interface Enrol<T> {

    /**
     * Write data to path.
     *
     * @param path
     * @param entity
     * @return
     */
    JsonObject write(final String path, final T entity);

    /**
     * Read data from path;
     *
     * @param path
     * @return
     */
    T read(final String path);
}
