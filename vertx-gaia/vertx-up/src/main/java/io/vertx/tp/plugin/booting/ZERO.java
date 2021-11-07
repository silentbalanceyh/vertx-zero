package io.vertx.tp.plugin.booting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<Class<?>, KBoot> BOOTS = new ConcurrentHashMap<>();
}
