package io.horizon.eon;

/**
 * @author lang : 2023-05-30
 */
interface VSpecBoot {

    String __KEY = "boot";

    String _ENV_DEVELOPMENT = ".env.development";

    String LAUNCHER = "launcher";
    String COMPONENT = VName.COMPONENT;
    String EXTENSION = VName.EXTENSION;
    String CONFIG = VName.CONFIG;
    String RAD = "rad";
    String CONNECT = "CONNECT";

    interface component {
        String ON = "on";
        String OFF = "off";
        String RUN = "run";
    }

    interface connect {
        String FRONTIER = "frontier";
        String GALAXY = "galaxy";
        String SPACE = "space";
    }

    interface extension {
        String EXECUTOR = "executor";
    }
}
