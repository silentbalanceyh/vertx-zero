package io.vertx.up.eon.em.run;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ActTime {
    BEFORE, // DataRegion before
    AFTER,  // DataRegion after
    AROUND,  // Around execution
    NONE,    // Disable dual mapping execution
}
