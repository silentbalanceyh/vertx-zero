package io.vertx.tp.plugin.elasticsearch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, ElasticSearchHelper> HELPERS
            = new ConcurrentHashMap<>();
}
