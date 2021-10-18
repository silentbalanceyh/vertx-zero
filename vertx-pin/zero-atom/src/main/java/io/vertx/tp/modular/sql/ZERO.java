package io.vertx.tp.modular.sql;

import io.vertx.core.json.JsonArray;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, String> DB_MAPPING =
        new ConcurrentHashMap<>();

    ConcurrentMap<String, JsonArray> DB_TYPE_MAPPING =
        new ConcurrentHashMap<>();

    ConcurrentMap<String, SqlTypeProvider> DB_TYPE_REF =
        new ConcurrentHashMap<>();
}
