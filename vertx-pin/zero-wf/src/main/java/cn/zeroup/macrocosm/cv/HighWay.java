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
        // Pending Only ( For Approve Edit )
        String TASK_QUEUE = KeIpc.Workflow.EVENT + "W-TODO/TASK/QUEUE";

        // History Only ( For History )
        String TASK_HISTORY = KeIpc.Workflow.EVENT + "W-TODO/TASK/HISTORY";

        // Fetch before creation
        String TASK_FORM = KeIpc.Workflow.EVENT + "W-TODO/TASK/VIRTUAL-FORM";
    }

    /*
     * Processing for writing
     */
    interface Do {
        // Start new workflow instance
        String FLOW_START = KeIpc.Workflow.EVENT + "WORKFLOW/START";

        // Complete and next workflow instance
        String FLOW_COMPLETE = KeIpc.Workflow.EVENT + "WORKFLOW/COMPLETE";

        // Saving for draft workflow instance
        String FLOW_DRAFT = KeIpc.Workflow.EVENT + "WORKFLOW/DRAFT";

        // Batching for draft workflow instance
        String FLOW_BATCH = KeIpc.Workflow.EVENT + "WORKFLOW/BATCH";

        // Cancel for workflow instance
        String FLOW_CANCEL = KeIpc.Workflow.EVENT + "WORKFLOW/CANCEL";
    }

    /*
     * Processing for definition
     */
    interface Flow {
        // Fetch Workflow by code
        String BY_CODE = KeIpc.Workflow.EVENT + "W-FLOW/BY/CODE";
        // Fetch Todo + Task based on todo Id
        String BY_TODO = KeIpc.Workflow.EVENT + "W-FLOW/BY/KEY";
        // Fetch Todo + History
        String BY_HISTORY = KeIpc.Workflow.EVENT + "W-FLOW/BY/HISTORY";
    }

    interface Todo {

        String BY_ID = KeIpc.Workflow.EVENT + "W-TODO/BY-ID";
    }
}
