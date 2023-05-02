package io.horizon.eon;

/**
 * @author lang : 2023/4/24
 */
public interface VMessage {

    interface Extractor {
        // ------------ io.vertx.up.uca.rs.config.Extractor
        String JOB_IGNORE = "[ Job ] The class {0} annotated with @Job will be ignored because there is no @On method defined.";

    }

    interface Job {
        interface PHASE {

            String UCA_COMPONENT = "[ Job ] {0} selected: {1}";
            // ------------ io.vertx.up.uca.job.phase.OutPut / Input
            String UCA_EVENT_BUS = "[ Job ] {0} event bus enabled: {1}";
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
            String ERROR_TERMINAL = "[ Job: {0} ] Terminal with error: {1}";
        }

        interface INTERVAL {
            // ------------ io.vertx.up.uca.job.timer.Interval
            String START = "[ Job ] (timer = null) The job will start right now.";
            String RESTART = "[ Job ] (timer = null) The job will restart right now.";
            String DELAY_START = "[ Job ] `{0}` will start after `{1}`.";
            String DELAY_RESTART = "[ Job ] `{0}` will restart after `{1}`.";
            String SCHEDULED = "[ Job ] (timer = {0}) `{1}` scheduled duration {2} ms in each.";
        }

        // ------------ io.vertx.up.atom.worker.Mission
        interface MISSION {
            String JOB_OFF = "[ Job ] Current job `{0}` has defined @Off method.";
        }

        // ------------ C: io.aeon.experiment.specification.sch.KTimer
        interface TIMER {
            String DELAY = "[ Job ] Job \"{0}\" will started after `{1}` ";
        }

        // ------------ io.vertx.up.uca.job.store.JobStore
        interface STORE {
            String SCANNED = "[ Job ] The system scanned {0} jobs with type {1}";
        }

        interface PIN {
            // ------------ io.vertx.up.uca.job.store.JobPin
            String PIN_CONFIG = "[ Job ] Job configuration read : {0}";
        }

        interface AGHA {
            // ------------ io.vertx.up.uca.job.center.Agha
            String MOVED = "[ Job ] [{0}]（ Moved: {2} -> {3} ）, Job = `{1}`";
            String TERMINAL = "[ Job ] [{0}] The job will be terminal, status -> ERROR, Job = `{1}`";
            String WORKER_START = "[ Job ] `{0}` worker executor will be created. The max executing time is {1} s";
            String WORKER_END = "[ Job ] `{0}` worker executor has been closed! ";
        }
    }

    // ===== Job Message from Backend

    // ----------- C: io.vertx.up.fn.Fn
    interface Fn {
        String PROGRAM_NULL = "[ Program ] Null Input";
        String PROGRAM_QR = "[ Program ] Null Record in database";
    }

    // ---------- C: io.vertx.up.runtime.ZeroMotor
    interface Motor {
        String AGENT_DEFINED = "User defined agent {0} of type = {1}, " +
            "the default will be overwritten.";
        String APP_CLUSTERD = "Current app is running in cluster mode, " +
            "manager = {0} on node {1} with status = {2}.";
        String SOCK_ENABLED = "( Micro -> Sock ) Zero system detected the socket server is Enabled.";
        String RPC_ENABLED = "( Micro -> Rpc ) Zero system detected the rpc server is Enabled. ";
    }

    // ---------- io.vertx.up.uca.options.Visitor
    interface Visitor {
        String V_BEFORE = "( node = {0}, type = {1} ) before validation is {2}.";
        String V_AFTER = "( node = {0}, type = {1} ) filtered configuration port set = {2}.";
    }

    // ---------- C: io.vertx.rx.web.anima.Verticles
    // ---------- C: io.vertx.up.uca.web.anima.Verticles
    interface Verticle {
        String END = "( {3} ) The verticle {0} has been deployed " +
            "{1} instances successfully. id = {2}.";
        String FAILED = "( {3} ) The verticle {0} has been deployed " +
            "{1} instances failed. id = {2}, cause = {3}.";
        String STOPPED = "( {2} ) The verticle {0} has been undeployed " +
            " successfully, id = {1}.";
    }

    // ---------- C: io.vertx.up.uca.di.DiPlugin
    interface DiPlugin {
        String IMPL_NULL = "The system scanned null infix for key = {0} " +
            "on the field \"{1}\" of {2}";
        String IMPL_WRONG = "The hitted class {0} does not implement the interface" +
            "of {1}";
    }
    // ------------ io.vertx.up.log.Measure

    interface Measure {
        String REMOVE = "[ Meansure ] The {0} has been removed. ( instances = {1} )";
        String ADD = "[ Meansure ] The {0} has been added. ( instances = {1} ), worker = {2}";

    }

    // ---------- io.vertx.up.uca.web.anima.Scatter
    interface Scatter {
        String CODEX = "( {0} Rules ) Zero system scanned the folder /codex/ " +
            "to pickup {0} rule definition files.";

    }

    // ---------- io.vertx.up.uca.web.origin.Inquirer
    interface Inquirer {
        String HQAS = "( {0} QaS ) The Zero system has found " +
            "{1} points of @QaS.";
        String ENDPOINT = "( {0} EndPoint ) The Zero system has found " +
            "{0} components of @EndPoint.";
        String WEBSOCKET = "( {0} WebSocket ) The Zero system has found " +
            "{0} components of @EndPoint.";
        String JOB = "( {0} Job ) The Zero system has found " +
            "{0} components of @Job.";
        String QUEUE = "( {0} Queue ) The Zero system has found " +
            "{0} components of @Queue.";
        String INJECTION = "( {1} Inject ) The Zero system has found \"{0}\" object contains " +
            "{1} components of @Inject or ( javax.inject.infix.* ).";
    }

    // ---------- io.horizon.uca.log.Annal
    interface Annal {
        String INTERNAL = "[ZERO] The inject Annal has not been configured, will select internal for {}.";
        String CONFIGURED = "[ZERO] The logger ( Annal = {} ) has been selected for {} to record logs.";
    }

    // ---------- io.horizon.specification.uca.HFS
    interface HFS {

        String IO_CMD_RM = "I/O Command: `rm -rf {0}`";
        String IO_CMD_MKDIR = "I/O Command: `mkdir -P {0}`";
        String IO_CMD_MOVE = "I/O Command: `mv {0} {1}`";

        String IO_CMD_CP = "I/O Command: `cp -rf {0} {1}`, Method = {2}";

        String ERR_CMD_CP = "One of folder: ({0},{1}) does not exist, could not execute cp command.";
    }
}
