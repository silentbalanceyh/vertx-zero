package io.horizon.eon.em.cloud;

import io.horizon.util.HaS;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum TypeOs {
    NA,             // NA
    MAC_OS,         // Mac Os
    UNIX,           // UNIX
    LINUX,          // LINUX
    WINDOWS;        // WINDOWS

    public static TypeOs from(final String os) {
        if (HaS.isNil(os)) {
            return TypeOs.NA;
        }
        if (os.startsWith("Windows")) {
            return TypeOs.WINDOWS;
        } else if (os.startsWith("Linux")) {
            return TypeOs.LINUX;
        } else if (os.startsWith("Mac")) {
            return TypeOs.MAC_OS;
        } else {
            return TypeOs.UNIX;
        }
    }
}
