package io.vertx.up.eon.em.run;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ActPhase {
    DELAY, // Delay for ACL control
    EAGER, // Eager for ACL control, this control should impact current request
    ERROR, // Error phase
}
