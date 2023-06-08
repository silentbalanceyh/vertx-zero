package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmBoot {
    private EmBoot() {
    }

    /**
     * @author lang : 2023-05-30
     */
    public enum LifeCycle {
        ON,     // install, resolved, start
        OFF,    // stop, uninstall
        RUN;    // run, update, refresh

        public static LifeCycle from(final String name) {
            return LifeCycle.valueOf(name.toUpperCase());
        }
    }
}
