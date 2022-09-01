package cn.vertxup.workflow.cv.em;

/*
 *
                            <Start>
                               |
                           「Pending」
                               |
                               |
                            <Long?>  ------- Long -------- 「ACCEPTED」
                               |                               |
                             Short                             |
                               |                               |
      「Rejected」-------- <Approval?> -----------------------  |
                               |
                          「Finished」

   Status for other situations:
    - 「CANCELED」
    - 「EXPIRED」
 *
 * 1. Here are status for todo tasks: The todo task contains following structure
 *    - X_TODO + Data ( Json ) + Form ( UI_FORM )
 *    - X_TODO + Data ( Json ) + Form ( UI_FORM ) + W_INSTANCE
 * 2. Data Record extracting way:
 *    - Static:  MODEL_COMPONENT -> MODEL_KEY
 *    - Dynamic: MODEL_ID -> MODEL_KEY
 *    - Camunda: INSTANCE -> ( Case Id in W_INSTANCE ), new workflow
 * 3. Form Config extracting way:
 *    - Method 1:  MODEL_FORM -> ( Code in UI_FORM ), dynamic form
 *    - Method 2:  MODEL_FORM -> ( filename stored ), static form
 *                            --- src/resources/plugin/ui/forms/
 *                            --- The MODEL_FORM stored the filename such as "request.data.form" ( Json Format )
 *                            --- The extension is synced with camunda of form ( XML Format ) but format different
 *    - Method 3: INSTANCE -> W_INSTANCE -> ( formKey in camunda ) to read form
 *                            --- The format is:  camunda-forms:deployment:<MODEL_FORM> here
 * 4. Op extracting way（ in camunda the `idea` will move the node ）:
 *    - Start Action          --- Start new workflow in camunda
 *    - Approval Action       --- Confirm for processing
 *    - Reject Action         --- Reject for processing
 *    - Complete Action ( Include closing )
 *                            --- Complete the task and move / trigger here
 *
 * 5. Todo mode:
 *
 *                  Todo        TodoItem         Record        Instance
 *    1.Single       o              x              o              x
 *    2.Workflow     o              o              o              o
 *    3.T/T          o              o              o              x
 *
 *     - For single todo, here are choice:
 *           Approval / Reject
 *     - For workflow:
 *           1 Instance + n Todo,  1 Todo + n TodoItem
 *     - For T/T:
 *           1 Todo + n TodoItem
 *
 *    T/T Mode:
 *    1 - Todo
 *    n - [ ]. TodoItem 1
 *      - [ ]. TodoItem 2
 *      - ......
 *      - [ ]. TodoItem n
 */
public enum TodoStatus {
    // Draft before starting
    DRAFT,
    // Wait for execute todo task
    PENDING,
    // Finished by user
    FINISHED,
    // Reject by user
    REJECTED,
    // Back by user
    REDO,
    // Cancelled by user or other reason
    CANCELED,
    // Expired by system
    EXPIRED,
    // Long time task for accept
    ACCEPTED
}
