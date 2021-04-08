package io.vertx.up.uca.rs.hunt.adaptor;

import io.vertx.up.util.Ut;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Pool {

    ConcurrentMap<String, ConcurrentMap<String, Wings>> SELECT_POOL = new ConcurrentHashMap<String, ConcurrentMap<String, Wings>>() {
        {
            /* Type `*` */
            this.put(MediaType.WILDCARD_TYPE.getType(), new ConcurrentHashMap<String, Wings>() {
                {
                    /* SubType `*` */
                    this.put(MediaType.WILDCARD_TYPE.getSubtype(), Ut.singleton(JsonWings.class));
                }
            });

            /* Type `application` */

            this.put(MediaType.APPLICATION_JSON_TYPE.getType(), new ConcurrentHashMap<String, Wings>() {
                {
                    /* SubType: json */
                    this.put(MediaType.APPLICATION_JSON_TYPE.getSubtype(), Ut.singleton(JsonWings.class));

                    /* SubType: octet-stream */
                    this.put(MediaType.APPLICATION_OCTET_STREAM_TYPE.getSubtype(), Ut.singleton(BufferWings.class));
                }
            });

        }
    };
}
