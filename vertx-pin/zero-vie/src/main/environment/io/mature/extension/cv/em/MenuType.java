package io.mature.extension.cv.em;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum MenuType {
    APP_MENU("APP-MENU"),
    TOP_MENU("TOP-MENU"),
    SIDE_MENU("SIDE-MENU"),
    NAV_MENU("NAV-MENU"),
    SC_MENU("SC-MENU"),
    DEV_MENU("DEV-MENU"),
    SYS_MENU("SYS-MENU"),
    EXTRA_MENU("EXTRA-MENU"),
    ENTRY_MENU("ENTRY-MENU"),
    BAG_MENU("BAG-MENU");


    private final static List<String> VALUES = new ArrayList<>() {
        {
            this.add(APP_MENU.value);
            this.add(TOP_MENU.value);
            this.add(SIDE_MENU.value);
            this.add(NAV_MENU.value);
            this.add(SC_MENU.value);
            this.add(DEV_MENU.value);
            this.add(SYS_MENU.value);
            this.add(ENTRY_MENU.value);
            this.add(EXTRA_MENU.value);
            this.add(BAG_MENU.value);
        }
    };
    private final transient String value;

    MenuType(final String value) {
        this.value = value;
    }

    public static List<String> valueDefault() {
        return VALUES;
    }

    public String value() {
        return this.value;
    }
}
