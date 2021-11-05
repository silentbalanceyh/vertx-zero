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
        // zero-erp
        String ERP = "erp";
        // zero-psi
        String PSI = "psi";
        // zero-fm
        String FM = "fm";
        // zero-ui
        String UI = "ui";
        // zero-jet
        String JET = "jet";
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
