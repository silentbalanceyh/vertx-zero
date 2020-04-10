package io.vertx.tp.ke.cv;

/**
 * Standard Ipc for Zero extension module
 * It's for communication
 */
public interface KeIpc {
    /*
     * Crud Ipc
     */
    interface Ix {

        /* Module Crud */
        interface Module {

        }
    }

    /*
     * Rbac Ipc
     */
    interface Sc {
        /* Ipc for verify token */
        String IPC_TOKEN_VERIFY = "IPC://TOKEN/VERIFY";
        /* Ipc for access token */
        String IPC_TOKEN_ACCESS = "IPC://TOKEN/ACCESS";

        /* Module Crud */
        interface Module {

        }
    }

    /*
     * Ambient Ipc
     */
    interface At {

        /* Module Crud */
        interface Module {

        }
    }

    /*
     * Location Service
     */
    interface Lb {

        /* Module Crud */
        interface Module {

        }
    }

    /*
     * Ui Ipc
     */
    interface Ui {

        /* Module Crud */
        interface Module {

        }
    }

    /*
     * Dynamic Routing ( Ox )
     */
    interface Jt {

        /* Module Crud */
        interface Module {

        }
    }
}
