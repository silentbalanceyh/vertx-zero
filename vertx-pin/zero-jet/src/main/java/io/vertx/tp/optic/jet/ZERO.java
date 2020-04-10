package io.vertx.tp.optic.jet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, JtIngest> POOL_INGEST = new ConcurrentHashMap<>();
}
