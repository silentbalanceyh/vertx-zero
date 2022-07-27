package io.vertx.aeon.eon.em;

import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum TypeOs {
    NA,             // NA
    UNIX,           // UNIX:  Mac Os
    LINUX,          // LINUX
    WINDOWS;        // WINDOWS

    public static TypeOs from(final String os) {
        if (Ut.isNil(os)) {
            return TypeOs.NA;
        }
        if (os.startsWith("Windows")) {
            return TypeOs.WINDOWS;
        } else if (os.startsWith("Linux")) {
            return TypeOs.LINUX;
        } else {
            return TypeOs.UNIX;
        }
    }
}
