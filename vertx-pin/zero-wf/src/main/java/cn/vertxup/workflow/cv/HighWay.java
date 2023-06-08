package cn.vertxup.workflow.cv;

import io.vertx.mod.ke.cv.KeIpc;

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

        // Fetch before creation
        String TASK_FORM = KeIpc.Workflow.EVENT + "W-TODO/TASK/VIRTUAL-FORM";

        // Related Ticket
        String TICKET_LINKAGE = KeIpc.Workflow.EVENT + "W-TICKET/TICKET/LINKAGE";

        // History Only ( For History )
        String TICKET_HISTORY = KeIpc.Workflow.EVENT + "W-TICKET/TICKET/HISTORY";
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

        // Close for workflow instance
        String FLOW_CLOSE = KeIpc.Workflow.EVENT + "WORKFLOW/CLOSE";
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

    interface Report {
        String TICKET_LIST = KeIpc.Workflow.EVENT + "REPORT/LIST";
        String TICKET_ACTIVITY = KeIpc.Workflow.EVENT + "REPORT/ACTIVITY";
        String ASSETS_LIST = KeIpc.Workflow.EVENT + "REPORT/ASSETS/LIST";
    }
}
