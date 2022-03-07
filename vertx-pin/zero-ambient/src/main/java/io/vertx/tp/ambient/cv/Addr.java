package io.vertx.tp.ambient.cv;

/*
 * Address for Event Bus of Ambient
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://περιβάλλων/";
}

public interface Addr {

    interface App {

        String BY_NAME = Prefix._EVENT + "X-APP/BY-NAME";

        String BY_ID = Prefix._EVENT + "X-APP/BY-ID";

        String UP_BY_ID = Prefix._EVENT + "X-APP/UPDATE/BY-ID";

    }

    interface Menu {

        String BY_APP_ID = Prefix._EVENT + "X-MENU/APP-ID";

        String MY_FETCH = Prefix._EVENT + "X-MENU/MY/FETCH";

        String MY_SAVE = Prefix._EVENT + "X-MENU/MY/SAVE";
    }

    interface Init {

        String PREPARE = Prefix._EVENT + "X-PREPARE";

        String INIT = Prefix._EVENT + "X-INIT";

        String CONNECT = Prefix._EVENT + "X-CONNECT";

        String SOURCE = Prefix._EVENT + "X-SOURCE";

        String DOCUMENT = Prefix._EVENT + "X-DOCUMENT/DOCUMENT";
    }

    interface File {

        String UPLOAD = Prefix._EVENT + "X-UPLOAD";

        String DOWNLOAD = Prefix._EVENT + "X-DOWNLOAD";

        String MY_QUEUE = Prefix._EVENT + "X-ATTACHMENT/MY/QUEUE";

        String BY_KEY = Prefix._EVENT + "X-ATTACHMENT/BY/KEY";
    }

    interface Datum {

        String CATEGORY_TYPE = Prefix._EVENT + "X-CATEGORY/TYPE";

        String CATEGORY_TYPES = Prefix._EVENT + "X-CATEGORY/TYPES";

        String CATEGORY_CODE = Prefix._EVENT + "X-CATEGORY/CODE";

        String TABULAR_TYPE = Prefix._EVENT + "X-TABULAR/TYPE";

        String TABULAR_TYPES = Prefix._EVENT + "X-TABULAR/TYPES";

        String TABULAR_CODE = Prefix._EVENT + "X-TABULAR/CODE";
    }

    interface Module {
        String BY_NAME = Prefix._EVENT + "X-MODULE/NAME";

        String MODELS = Prefix._EVENT + "X-MODEL/LIST";

        String MODEL_FIELDS = Prefix._EVENT + "X-MODEL/FIELDS";
    }

    interface History {

        String HISTORIES = Prefix._EVENT + "X-ACTIVITY/HISTORIES";

        String HISTORY_ITEMS = Prefix._EVENT + "X-ACTIVITY/HISTORY-ITEMS";

        String HISTORY_BY_FIELDS = Prefix._EVENT + "X-ACTIVITY/HISTORY-BY-FIELDS";

        String ACTIVITY_SEARCH = Prefix._EVENT + "X-ACTIVITY/SEARCH";

        String ACTIVITY_GET = Prefix._EVENT + "X-ACTIVITY/GET";
    }

    interface Linkage {
        // Linkage fetch by sigma ( All linkage )
        String FETCH_BY_TYPE = Prefix._EVENT + "X-LINKAGE/BY/TYPE";

        // Linkage fetch Target / Source
        String FETCH_TARGET = Prefix._EVENT + "X-LINKAGE/TARGET/FETCH";
        String FETCH_SOURCE = Prefix._EVENT + "X-LINKAGE/SOURCE/FETCH";
        String FETCH_ST = Prefix._EVENT + "X-LINKAGE/T-S/FETCH";

        // Linkage fetch by key
        String FETCH_BY_KEY = Prefix._EVENT + "X-LINKAGE/KEY/FETCH";
        String REMOVE_BY_REGION = Prefix._EVENT + "X-LINKAGE/REGION/REMOVE";

        // Linkage fetch by source_key/target_key
        String ADD_NEW_B = Prefix._EVENT + "X-LINKAGE/ADD/NEW-B";
        String ADD_NEW_V = Prefix._EVENT + "X-LINKAGE/ADD/NEW-V";
        String SAVE_BATCH_B = Prefix._EVENT + "X-LINKAGE/BATCH/SAVING-B";
        String SAVE_BATCH_V = Prefix._EVENT + "X-LINKAGE/BATCH/SAVING-V";

        String SYNC_B = Prefix._EVENT + "X-LINKAGE/SYNC-B";
    }
}
