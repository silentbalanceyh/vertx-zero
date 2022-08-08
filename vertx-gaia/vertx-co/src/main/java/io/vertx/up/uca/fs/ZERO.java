package io.vertx.up.uca.fs;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Info {
    String IO_CMD_RM = "I/O Command: `rm -rf {0}`";
    String IO_CMD_MKDIR = "I/O Command: `mkdir -P {0}`";
    String IO_CMD_MOVE = "I/O Command: `mv {0} {1}`";

    String IO_CMD_CP = "I/O Command: `cp -rf {0} {1}`, Method = {2}";

    String ERR_CMD_CP = "One of folder: ({0},{1}) does not exist, could not execute cp command.";
}
