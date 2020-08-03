package io.vertx.tp.rbac.cv;

/**
 * Address for Event Bus
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Ασφάλεια/";
}

public interface Addr {

    interface Rule {
        /*
         * Fetch all rules that defined in current app
         */
        String FETCH_BY_SIGMA = Prefix._EVENT + "X-RULE/FETCH/ALL";
        String FETCH_RULE_ITEMS = Prefix._EVENT + "X-RULE-ITEM/FETCH/BY/RULE";
        /*
         * Fetch all resource definition
         */
        String FETCH_VIEWS = Prefix._EVENT + "X-RULE-VIEW/FETCH/BY-KEYS";
        String FETCH_VISITANT = Prefix._EVENT + "X-VISITANT/FETCH/OWNER";
    }

    interface Auth {

        String LOGIN = Prefix._EVENT + "O-LOGIN";

        String LOGOUT = Prefix._EVENT + "O-LOGOUT";

        String AUTHORIZE = Prefix._EVENT + "O-AUTHORIZE";

        String TOKEN = Prefix._EVENT + "O-TOKEN";
    }

    interface User {
        String INFORMATION = Prefix._EVENT + "X-INFORMATION";

        String PASSWORD = Prefix._EVENT + "X-PASSWORD";

        String PROFILE = Prefix._EVENT + "X-PROFILE";

        String SEARCH = Prefix._EVENT + "X-SEARCH";

        /**
         * modified by Hongwei at 2019/12/06
         * add get, create, update and delete methods for user domain.
         */
        String GET = Prefix._EVENT + "X-USER/GET/ID";

        String ADD = Prefix._EVENT + "X-USER/ADD";

        String DELETE = Prefix._EVENT + "X-DELETE/USER/ID";

        String UPDATE = Prefix._EVENT + "X-PUT/USER/ID";

        String IMPORT = Prefix._EVENT + "X-IMPORT/USER";
    }

    interface Authority {
        /* Api Seeking（Action Only） */
        String ACTION_SEEK = Prefix._EVENT + "X-ACTION/SEEK";

        /* Api Pre-Ready */
        String ACTION_READY = Prefix._EVENT + "X-ACTION/READY";

        /* Resource Search */
        String RESOURCE_SEARCH = Prefix._EVENT + "X-RESOURCE/SEARCH";

        /* Perm Information */
        String PERMISSION_GROUP = Prefix._EVENT + "X-PERMISSION/GROUP";
        /* Perm Saving, Save permission definition */
        String PERMISSION_DEFINITION_SAVE = Prefix._EVENT + "X-PERMISSION/DEFINITION/SAVING";

        /* Get all relation between role & permission */
        String PERMISSION_BY_ROLE = Prefix._EVENT + "X-PERMISSION/BY/ROLE";
        /* Save all relation between role & permission */
        String PERMISSION_SAVE = Prefix._EVENT + "X-PERMISSION/SAVING";

        /* Resource get with action */
        String RESOURCE_GET_CASCADE = Prefix._EVENT + "X-RESOURCE/GET-CASCADE";
        /* Resource add with action */
        String RESOURCE_ADD_CASCADE = Prefix._EVENT + "X-RESOURCE/ADD-CASCADE";
        /* Resource update with action */
        String RESOURCE_UPDATE_CASCADE = Prefix._EVENT + "X-RESOURCE/UPDATE-CASCADE";
        /* Resource delete with action */
        String RESOURCE_DELETE_CASCADE = Prefix._EVENT + "X-RESOURCE/DELETE-CASCADE";

        String VIEW_UPDATE_BY_TYPE = Prefix._EVENT + "S-VIEW-BY-TYPE/PUT";
    }

    interface Group {
        String GROUP_SIGMA = Prefix._EVENT + "S-GROUP/SIGMA";
    }

    interface Role {
        String ROLE_SIGMA = Prefix._EVENT + "S-ROLE/SIGMA";

        String ROLE_PERM_UPDATE = Prefix._EVENT + "S-ROLE-PERM/PUT";
    }
}
