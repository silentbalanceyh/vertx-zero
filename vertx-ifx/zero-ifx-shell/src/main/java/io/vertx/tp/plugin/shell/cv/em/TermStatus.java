package io.vertx.tp.plugin.shell.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public enum TermStatus {
    SUCCESS,    // Success capture input line
    FAILURE,    // Failure happened
    WAIT,       // Wait for input status processing
    EXIT,       // Will quit current mode
}
