package io.vertx.up.uca.job.plugin;

interface INFO {

    String IS_RUNNING = "( Job ) The job {0} has already been running !!!";

    String IS_STARTING = "( Job ) The job {0} is booting, please preparing for READY";

    String IS_ERROR = "( Job ) The job {0} met error last time, please contact administrator and try to resume.";

    String IS_STOPPED = "( Job ) The timeId {0} does not exist in RUNNING pool of jobs.";

    String NOT_RUNNING = "( Job ) The job {0} is not running, the status is = {1}";
}
