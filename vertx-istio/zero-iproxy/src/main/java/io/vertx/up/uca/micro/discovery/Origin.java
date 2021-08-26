package io.vertx.up.uca.micro.discovery;

import io.vertx.servicediscovery.Record;

import java.util.concurrent.ConcurrentMap;

/**
 * Service discovery metadata discovery
 * 1. Backend : The worker will publish service out.
 * 2. Frontend : The api gateway will do discovery
 */
public interface Origin {
    String HOST = "host";
    String NAME = "name";
    String PORT = "port";
    String META = "metadata";
    String ID = "id";
    String PATH = "path";

    /**
     * Get backend
     *
     * @return Return registry data in etcd3
     */
    ConcurrentMap<String, Record> getRegistryData();

    /**
     * Erase record
     *
     * @param record discovery record that will be erased
     *
     * @return whether erasing successfully
     */
    boolean erasing(Record record);
}
