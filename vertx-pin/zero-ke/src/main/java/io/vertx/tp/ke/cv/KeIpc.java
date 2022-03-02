package io.vertx.tp.ke.cv;

/**
 * Standard Ipc for Zero extension module
 * It's for communication
 */
public interface KeIpc {
    interface Workflow {
        /*
         * Event Addr Prefix for workflow
         * This value will be shared between zero-ambient / zero-wf
         * */
        String EVENT = "Ἀτλαντὶς νῆσος://Ροή εργασίας/";
    }

    /* Module Crud */
    interface Module {
        // zero-is
        String IS = "is";
        // zero-battery
        String BATTERY = "battery";
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
        // zero-graphic
        String G = "graphic";
        // zero-atom
        String ATOM = "atom";
        // zero-lbs
        String LBS = "lbs";
        // zero-wf
        String WF = "wf";
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
