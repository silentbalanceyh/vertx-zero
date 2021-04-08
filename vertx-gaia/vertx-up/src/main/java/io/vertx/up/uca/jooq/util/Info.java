package io.vertx.up.uca.jooq.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Info {
    String INQUIRY_MESSAGE = "( Inquiry ) Processed metadata = {0}.";
}

interface Pool {
    ConcurrentMap<String, JqFlow> POOL_FLOW = new ConcurrentHashMap<>();
}
