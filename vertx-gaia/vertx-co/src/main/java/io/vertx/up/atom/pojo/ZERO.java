package io.vertx.up.atom.pojo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Info {

    String VALUE_SAME = "[V] Warning for duplicated mapper size: keys = {0}, values = {1}";
}

interface Pool {

    ConcurrentMap<String, Mojo> MOJOS =
        new ConcurrentHashMap<>();
}
