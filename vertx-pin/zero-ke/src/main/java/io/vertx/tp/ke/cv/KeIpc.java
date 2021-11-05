package io.vertx.tp.ke.cv;

/**
 * Standard Ipc for Zero extension module
 * It's for communication
 */
public interface KeIpc {
    /* Module Crud */
    interface Module {
        // zero-ambient
        String AMBIENT = "ambient";
        // zero-rbac
        String RBAC = "rbac";
    }

    /*
     * Rbac Ipc
     */
    interface Sc {
        /* Ipc for verify token */
        String IPC_TOKEN_VERIFY = "IPC://TOKEN/VERIFY";
        /* Ipc for access token */
        String IPC_TOKEN_ACCESS = "IPC://TOKEN/ACCESS";
    }
}
