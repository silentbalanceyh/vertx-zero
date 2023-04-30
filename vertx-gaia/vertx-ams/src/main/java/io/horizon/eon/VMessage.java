package io.horizon.eon;

/**
 * @author lang : 2023/4/24
 */
public interface VMessage {
    // ---------- io.horizon.log.Annal
    String ANNAL_INTERNAL = "[ZERO] The inject Annal has not been configured, will select internal for {0}.";
    String ANNAL_CONFIGURED = "[ZERO] The logger ( Annal = {0} ) has been selected for {1} to record logs.";


    // ---------- io.vertx.up.uca.web.origin.Inquirer
    String INQUIRER_HQAS = "( {0} QaS ) The Zero system has found " +
        "{1} points of @QaS.";
    String INQUIRER_ENDPOINT = "( {0} EndPoint ) The Zero system has found " +
        "{0} components of @EndPoint.";
    String INQUIRER_WEBSOCKET = "( {0} WebSocket ) The Zero system has found " +
        "{0} components of @EndPoint.";
    String INQUIRER_JOB = "( {0} Job ) The Zero system has found " +
        "{0} components of @Job.";
    String INQUIRER_QUEUE = "( {0} Queue ) The Zero system has found " +
        "{0} components of @Queue.";
    String INQUIRER_INJECTION = "( {1} Inject ) The Zero system has found \"{0}\" object contains " +
        "{1} components of @Inject or ( javax.inject.infix.* ).";


    // ---------- io.vertx.up.uca.web.anima.Scatter
    String SCATTER_CODEX = "( {0} Rules ) Zero system scanned the folder /codex/ " +
        "to pickup {0} rule definition files.";


    // ---------- C: io.vertx.up.uca.di.DiPlugin
    String DI_PLUGIN_NULL = "The system scanned null infix for key = {0} " +
        "on the field \"{1}\" of {2}";
    String DI_PLUGIN_IMPL = "The hitted class {0} does not implement the interface" +
        "of {1}";


    // ---------- C: io.vertx.rx.web.anima.Verticles
    // ---------- C: io.vertx.up.uca.web.anima.Verticles
    String VERTX_END = "( {3} ) The verticle {0} has been deployed " +
        "{1} instances successfully. id = {2}.";
    String VERTX_FAIL = "( {3} ) The verticle {0} has been deployed " +
        "{1} instances failed. id = {2}, cause = {3}.";
    String VERTX_STOPPED = "( {2} ) The verticle {0} has been undeployed " +
        " successfully, id = {1}.";

    // ---------- io.vertx.up.uca.options.Visitor
    String VISITOR_V_BEFORE = "( node = {0}, type = {1} ) before validation is {2}.";
    String VISITOR_V_AFTER = "( node = {0}, type = {1} ) filtered configuration port set = {2}.";


    // ---------- C: io.vertx.up.runtime.ZeroMotor
    String MOTOR_AGENT_DEFINED = "User defined agent {0} of type = {1}, " +
        "the default will be overwritten.";
    String MOTOR_APP_CLUSTERD = "Current app is running in cluster mode, " +
        "manager = {0} on node {1} with status = {2}.";
    String MOTOR_SOCK_ENABLED = "( Micro -> Sock ) Zero system detected the socket server is Enabled.";
    String MOTOR_RPC_ENABLED = "( Micro -> Rpc ) Zero system detected the rpc server is Enabled. ";


    // ----------- C: io.vertx.up.fn.Fn
    String PROGRAM_NULL = "[ Program ] Null Input";
    String PROGRAM_QR = "[ Program ] Null Record in database";

    // ===== Job Message from Backend
    // ------------ C: io.aeon.experiment.specification.sch.KTimer
    String JOB_DELAY = "[ Job ] Job \"{0}\" will started after `{1}` ";

    // ------------ io.vertx.up.uca.job.store.JobStore
    String JOB_STORE_SCANNED = "[ Job ] The system scanned {0} jobs with type {1}";

    // ------------ io.vertx.up.atom.worker.Mission
    String MISSION_JOB_OFF = "[ Job ] Current job `{0}` has defined @Off method.";

    // ------------ io.vertx.up.uca.rs.config.Extractor
    String EXTRACTOR_JOB_IGNORE = "[ Job ] The class {0} annotated with @Job will be ignored because there is no @On method defined.";

    // ------------ io.vertx.up.uca.job.store.JobPin
    String JOB_PIN_CONFIG = "[ Job ] Job configuration read : {0}";

    // ------------ io.vertx.up.uca.job.center.Agha
    String AGHA_MOVED = "[ Job ] [{0}]（ Moved: {2} -> {3} ）, Job = `{1}`";

    String AGHA_TERMINAL = "[ Job ] [{0}] The job will be terminal, status -> ERROR, Job = `{1}`";

    String AGHA_WORKER_START = "[ Job ] `{0}` worker executor will be created. The max executing time is {1} s";

    String AGHA_WORKER_END = "[ Job ] `{0}` worker executor has been closed! ";


    // ------------ io.vertx.up.uca.job.phase.OutPut / Input

    String IO_JOB_COMPONENT = "[ Job ] {0} selected: {1}";

    String IO_JOB_EVENT_BUS = "[ Job ] {0} event bus enabled: {1}";

    // ------------ io.vertx.up.uca.job.timer.Interval
    String ITL_START = "[ Job ] (timer = null) The job will start right now.";
    String ITL_RESTART = "[ Job ] (timer = null) The job will restart right now.";

    String ITL_DELAY_START = "[ Job ] `{0}` will start after `{1}`.";
    String ITL_DELAY_RESTART = "[ Job ] `{0}` will restart after `{1}`.";

    String ITL_SCHEDULED = "[ Job ] (timer = {0}) `{1}` scheduled duration {2} ms in each.";

    // ------------ io.vertx.up.uca.job.phase.*
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

    // ------------ io.vertx.up.log.Meansure
    String MEANSURE_REMOVE = "[ Meansure ] The {0} has been removed. ( instances = {1} )";
    String MEANSURE_ADD = "[ Meansure ] The {0} has been added. ( instances = {1} ), worker = {2}";

}
