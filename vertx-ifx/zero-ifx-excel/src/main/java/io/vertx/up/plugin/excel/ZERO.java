package io.vertx.up.plugin.excel;

import io.vertx.up.plugin.booting.KConnect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, KConnect> CONNECTS = new ConcurrentHashMap<>();
}
