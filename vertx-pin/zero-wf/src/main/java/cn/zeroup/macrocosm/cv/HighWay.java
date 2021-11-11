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
        String BY_CREATED = KeIpc.Workflow.EVENT + "W-TODO/BY/CREATED";

        // Fetch before creation
        String TASK_FORM = KeIpc.Workflow.EVENT + "W-TODO/BY/VIRTUAL-FORM";
    }

    /*
     * Processing for writing
     */
    interface Do {

    }

    /*
     * Processing for definition
     */
    interface Flow {
        // Fetch Workflow by code
        String BY_CODE = KeIpc.Workflow.EVENT + "W-FLOW/BY/CODE";
    }

    interface Todo {

        String BY_ID = KeIpc.Workflow.EVENT + "W-TODO/BY-ID";
    }
}
