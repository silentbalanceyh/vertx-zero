package io.vertx.mod.ke.cv;

import io.vertx.core.json.JsonArray;

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
        // zero-tpl
        String TPL = "tpl";
        // zero-crud
        String CRUD = "crud";
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

    interface Audit {

        JsonArray INCLUDE = new JsonArray()
            .add("/api/user")                           // zero-rbac
            .add("/api/permission")                     // zero-rbac
            .add("/api/authority/region/:path")         // zero-rbac
            .add("/api/employee")                       // zero-erp
            .add("/api/wh")                             // zero-psi
            .add("/api/i-directory")                    // zero-is
            .add("/api/file/upload")                    // zero-ambient
            .add("/api/my/menu/save")                   // zero-ambient
            .add("/api/up/flow")                        // zero-wf
            .add("/api/linkage/sync")                   // zero-wf, zero-ambient
            .add("/api/bill/")                          // zero-fm
            .add("/api/bill-item/")                     // zero-fm
            .add("/api/settle/")                        // zero-fm
            .add("/api/payment");                       // zero-fm

        JsonArray EXCLUDE = new JsonArray()
            .add("/api/:actor/search")                  // zero-crud
            .add("/api/:actor/missing")                 // zero-crud
            .add("/api/:actor/existing")                // zero-crud
            .add("/api/:actor/export")                  // zero-crud
            .add("/api/:actor/import")                  // zero-crud
            .add("/api/up/flow-queue")                  // zero-wf
            .add("/api/up/flow-history")                // zero-wf
            .add("/api/user/search/:identifier")        // zero-rbac
            ;
    }
}
