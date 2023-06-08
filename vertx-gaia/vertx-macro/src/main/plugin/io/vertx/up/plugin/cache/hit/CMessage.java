package io.vertx.up.plugin.cache.hit;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.buffer.Buffer;

import java.util.TreeSet;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
    String dataKey();

    /*
     * Data Type
     */
    <T> Class<T> dataType();

    // -------------- Checking Method -------------
    /*
     * Check whether current message is collection
     */
    boolean isList();

    /*
     * Check whether current message is reference
     */
    boolean isRef();
}
