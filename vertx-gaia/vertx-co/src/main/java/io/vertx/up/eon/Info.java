package io.vertx.up.eon;

public interface Info {

    String H_SCANED_QAS = "( {0} QaS ) The Zero system has found " +
        "{1} points of @QaS.";

    String SCANED_RULE = "( {0} Rules ) Zero system scanned the folder /codex/ " +
        "to pickup {0} rule definition files.";
    String INFIX_NULL = "The system scanned null infix for key = {0} " +
        "on the field \"{1}\" of {2}";

    String INFIX_IMPL = "The hitted class {0} does not implement the interface" +
        "of {1}";

    String VTC_END = "( {3} ) The verticle {0} has been deployed " +
        "{1} instances successfully. id = {2}.";
    String VTC_FAIL = "( {3} ) The verticle {0} has been deployed " +
        "{1} instances failed. id = {2}, cause = {3}.";

    String VTC_STOPPED = "( {2} ) The verticle {0} has been undeployed " +
        " successfully, id = {1}.";

    String INF_B_VERIFY = "( node = {0}, type = {1} ) before validation is {2}.";

    String INF_A_VERIFY = "( node = {0}, type = {1} ) filtered configuration port set = {2}.";

    String AGENT_DEFINED = "User defined agent {0} of type = {1}, " +
        "the default will be overwritten.";

    String SCANED_ENDPOINT = "( {0} EndPoint ) The Zero system has found " +
        "{0} components of @EndPoint.";

    String SCANED_WEBSOCKET = "( {0} WebSocket ) The Zero system has found " +
        "{0} components of @EndPoint.";

    String SCANED_JOB = "( {0} Job ) The Zero system has found " +
        "{0} components of @Job.";

    String SCANED_QUEUE = "( {0} Queue ) The Zero system has found " +
        "{0} components of @Queue.";

    String SCANED_INJECTION = "( {1} Inject ) The Zero system has found \"{0}\" object contains " +
        "{1} components of @Inject or ( javax.inject.infix.* ).";

    String APP_CLUSTERD = "Current app is running in cluster mode, " +
        "manager = {0} on node {1} with status = {2}.";

    String SOCK_ENABLED = "( Micro -> Sock ) Zero system detected the socket server is Enabled.";

    String RPC_ENABLED = "( Micro -> Rpc ) Zero system detected the rpc server is Enabled. ";

    // ----------- Job related information

    String JOB_IGNORE = "[ Job ] The class {0} annotated with @Job will be ignored because there is no @On method defined.";

    String JOB_CONFIG = "[ Job ] Job configuration read : {0}";

    String JOB_MOVED = "[ Job ] [{0}]（ Moved: {2} -> {3} ）, Job = `{1}`";

    String JOB_TERMINAL = "[ Job ] [{0}] The job will be terminal, status -> ERROR, Job = `{1}`";

    String JOB_COMPONENT_SELECTED = "[ Job ] {0} selected: {1}";

    String JOB_ADDRESS_EVENT_BUS = "[ Job ] {0} event bus enabled: {1}";

    String JOB_WORKER_START = "[ Job ] `{0}` worker executor will be created. The max executing time is {1} s";

    String JOB_WORKER_END = "[ Job ] `{0}` worker executor has been closed! ";

    // ------------- Job monitor for interval component
    String JOB_RUN = "[ Job ] (timer = null) The job will start right now.";
    String JOB_RUN_RE = "[ Job ] (timer = null) The job will restart right now.";

    String JOB_RUN_DELAY = "[ Job ] `{0}` will start after `{1}`.";
    String JOB_RUN_RE_DELAY = "[ Job ] `{0}` will restart after `{1}`.";

    String JOB_RUN_SCHEDULED = "[ Job ] (timer = {0}) `{1}` scheduled duration {2} ms in each.";

    // ------------- Job monitor for ONCE
    String PHASE_1ST_JOB = "[ Job: {0} ] 1. Input new data of JsonObject";

    String PHASE_1ST_JOB_ASYNC = "[ Job: {0} ] 1. Input from address {1}";

    String PHASE_2ND_JOB = "[ Job: {0} ] 2. Input without `JobIncome`";

    String PHASE_2ND_JOB_ASYNC = "[ Job: {0} ] 2. Input with `JobIncome` = {1}";

    String PHASE_3RD_JOB_RUN = "[ Job: {0} ] 3. --> @On Method call {1}";

    String PHASE_6TH_JOB_CALLBACK = "[ Job: {0} ] 6. --> @Off Method call {1}";

    String PHASE_4TH_JOB_ASYNC = "[ Job: {0} ] 4. Output with `JobOutcome` = {1}";

    String PHASE_4TH_JOB = "[ Job: {0} ] 4. Output without `JobOutcome`";

    String PHASE_5TH_JOB = "[ Job: {0} ] 5. Output directly, ignore next EventBus steps";

    String PHASE_5TH_JOB_ASYNC = "[ Job: {0} ] 5. Output send to address {1}";

    String PHASE_ERROR = "[ Job: {0} ] Terminal with error: {1}";

    // ------------ Job
    String JOB_DELAY = "[ Job ] Job \"{0}\" will started after `{1}` ";

    String JOB_SCANNED = "[ Job ] The system scanned {0} jobs with type {1}";

    String JOB_OFF = "[ Job ] Current job `{0}` has defined @Off method.";

    // ------------- Meansure
    String MEANSURE_REMOVE = "[ Meansure ] The {0} has been removed. ( instances = {1} )";
    String MEANSURE_ADD = "[ Meansure ] The {0} has been added. ( instances = {1} ), worker = {2}";

    // ------------- Program
    String PROGRAM_NULL = "[ Program ] Null Input";
    String PROGRAM_QR = "[ Program ] Null Record in database";
}
