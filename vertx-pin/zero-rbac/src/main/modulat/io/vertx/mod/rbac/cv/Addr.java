package io.vertx.mod.rbac.cv;

/**
 * Address for Event Bus
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Ασφάλεια/";
}

public interface Addr {

    interface Rule {

        /*
         * 「New Version」
         * 1. Fetch for KPermit of new structure
         * 2. Save View based on Configuration
         */
        String FETCH_REGION = Prefix._EVENT + "X-RULE/FETCH/REGION";
        String FETCH_REGION_VALUES = Prefix._EVENT + "X-RULE/FETCH/REGION-VALUES";

        String FETCH_REGION_DEFINE = Prefix._EVENT + "X-RULE/FETCH/REGION-DEFINE";

        String SAVE_REGION = Prefix._EVENT + "X-RULE/SAVING/SINGLE";
    }

    interface Auth {

        String LOGIN = Prefix._EVENT + "O-LOGIN";

        String LOGOUT = Prefix._EVENT + "O-LOGOUT";

        String AUTHORIZE = Prefix._EVENT + "O-AUTHORIZE";

        String TOKEN = Prefix._EVENT + "O-TOKEN";

        String CAPTCHA_IMAGE = Prefix._EVENT + "X-CAPTCHA/IMAGE";

        String CAPTCHA_IMAGE_VERIFY = Prefix._EVENT + "X-CAPTCHA/IMAGE/VERIFY";

        String CAPTCHA_SMS = Prefix._EVENT + "X-CAPTCHA/SMS";
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

        /*
         * New for user processing
         */
        String QR_USER_SEARCH = Prefix._EVENT + "X-USER/QR/SEARCH";
    }

    interface Perm {
        /* Search all permissions that are not related */
        String PERMISSION_UN_READY = Prefix._EVENT + "X-PERMISSION/UN-READY";

        /*
         * CRUD replaced
         */
        String BY_ID = Prefix._EVENT + "X-PERMISSION/CRUD/READ";
        String ADD = Prefix._EVENT + "X-PERMISSION/CRUD/CREATE";
        String EDIT = Prefix._EVENT + "X-PERMISSION/CRUD/UPDATE";
        String DELETE = Prefix._EVENT + "X-PERMISSION/CRUD/DELETE";
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
    }

    interface View {
        /*
         * View interface publish for `my view` instead of old `my`
         */
        String VIEW_P_BY_USER = Prefix._EVENT + "X-VIEW-P/GET/BY-USER";
        String VIEW_P_ADD = Prefix._EVENT + "X-VIEW-P/ADD";
        String VIEW_P_DELETE = Prefix._EVENT + "X-VIEW-P/DELETE";
        String VIEW_P_UPDATE = Prefix._EVENT + "X-VIEW-P/UPDATE";
        String VIEW_P_BY_ID = Prefix._EVENT + "X-VIEW-P/GET/BY-ID";
        String VIEW_P_BATCH_DELETE = Prefix._EVENT + "X-VIEW-P/BATCH/DELETE";
        String VIEW_P_EXISTING = Prefix._EVENT + "X-VIEW-P/EXISTING";
    }

    interface Group {
        String GROUP_SIGMA = Prefix._EVENT + "S-GROUP/SIGMA";
    }

    interface Role {
        String ROLE_SIGMA = Prefix._EVENT + "S-ROLE/SIGMA";

        String ROLE_PERM_UPDATE = Prefix._EVENT + "S-ROLE-PERM/PUT";
    }
}
