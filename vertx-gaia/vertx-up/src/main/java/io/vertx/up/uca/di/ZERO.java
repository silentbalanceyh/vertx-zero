package io.vertx.up.uca.di;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Info {
    String INFIX_NULL = "The system scanned null infix for key = {0} " +
            "on the field \"{1}\" of {2}";

    String INFIX_IMPL = "The hitted class {0} does not implement the interface" +
            "of {1}";
}

interface Pool {

    ConcurrentMap<Class<?>, DiPlugin> PLUGINS = new ConcurrentHashMap<>();
    ConcurrentMap<Class<?>, DiScanner> INJECTION = new ConcurrentHashMap<>();

    ConcurrentMap<Class<?>, DiAnchor> INFIXES = new ConcurrentHashMap<>();
}
