package io.vertx.tp.plugin.shell.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public enum CommandType {
    SYSTEM,     // It means current command could get to sub-system of zero
    COMMAND,    // Current command is executor for plugin here
    DEFAULT;    // Default command that zero framework provide
}
