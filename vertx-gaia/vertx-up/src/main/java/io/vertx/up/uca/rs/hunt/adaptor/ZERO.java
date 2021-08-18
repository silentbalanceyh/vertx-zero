package io.vertx.up.uca.rs.hunt.adaptor;

import io.vertx.up.fn.Fn;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {
    ConcurrentMap<String, Wings> POOL_THREAD = new ConcurrentHashMap<>();

    ConcurrentMap<String, ConcurrentMap<String, Supplier<Wings>>> SELECT_POOL = new ConcurrentHashMap<String, ConcurrentMap<String, Supplier<Wings>>>() {
        {
            /* Type `*` */
            this.put(MediaType.WILDCARD_TYPE.getType(), new ConcurrentHashMap<String, Supplier<Wings>>() {
                {
                    /* SubType `*` */
                    this.put(MediaType.WILDCARD_TYPE.getSubtype(),
                            () -> Fn.poolThread(POOL_THREAD, JsonWings::new, MediaType.WILDCARD_TYPE.toString()));
                }
            });

            /* Type `application` */

            this.put(MediaType.APPLICATION_JSON_TYPE.getType(), new ConcurrentHashMap<String, Supplier<Wings>>() {
                {
                    /* SubType: json */
                    this.put(MediaType.APPLICATION_JSON_TYPE.getSubtype(),
                            () -> Fn.poolThread(POOL_THREAD, JsonWings::new, MediaType.APPLICATION_JSON_TYPE.toString()));

                    /* SubType: octet-stream */
                    this.put(MediaType.APPLICATION_OCTET_STREAM_TYPE.getSubtype(),
                            () -> Fn.poolThread(POOL_THREAD, BufferWings::new, MediaType.APPLICATION_OCTET_STREAM_TYPE.toString()));
                }
            });
        }
    };
}
