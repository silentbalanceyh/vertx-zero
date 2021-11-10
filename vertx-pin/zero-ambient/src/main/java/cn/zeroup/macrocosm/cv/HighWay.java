package cn.zeroup.macrocosm.cv;

import io.vertx.tp.ke.cv.KeIpc;

/**
 * To avoid duplicated with Addr class, here provide new class named 'HighWay'
 * for address value on event bus, the same usage objective for 'Addr'
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */

public interface HighWay {
    /*
     * Queue for reading
     */
    interface Queue {
        // Fetch by who created: CREATED_BY
        String BY_CREATED = KeIpc.Workflow.EVENT + "X-TODO/BY/CREATED";
    }

    /*
     * Processing for writing
     */
    interface Do {

    }
}
