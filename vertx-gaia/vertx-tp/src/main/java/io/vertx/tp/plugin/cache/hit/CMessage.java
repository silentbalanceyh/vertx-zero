package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.TreeSet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Calculated cache key based on L1Config etc
 */
public interface CMessage {
    // -------------- Bind Data -------------
    /*
     * Bind Meta Part
     */
    CMessage bind(TreeSet<String> primaryKeys);

    /*
     * Bind Data Part
     */
    <T> CMessage data(T data);

    // -------------- Get Data -------------
    /*
     * Data Delivery to Event Bus
     */
    Buffer dataDelivery(ChangeFlag flag);

    /*
     * Data Part get
     */
    <T> T data();

    /*
     * Cache Key unique here
     */
    String dataUnique();

    /*
     * Data Type
     */
    <T> Class<T> dataType();

    /*
     * Check whether current message is collection
     */
    boolean isList();

    /*
     * Check whether current message is reference
     */
    boolean isRef();
}
