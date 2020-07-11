package io.vertx.tp.ambient.cv;

/*
 * Address for Event Bus of Ambient
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://περιβάλλων/";
}

public interface Addr {

    interface Todo {

        String BY_ID = Prefix._EVENT + "X-TODO/BY-ID";
    }

    interface App {

        String BY_NAME = Prefix._EVENT + "X-APP/BY-NAME";

        String BY_ID = Prefix._EVENT + "X-APP/BY-ID";

    }

    interface Menu {

        String BY_APP_ID = Prefix._EVENT + "X-MENU/APP-ID";
    }

    interface Init {

        String PREPARE = Prefix._EVENT + "X-PREPARE";

        String INIT = Prefix._EVENT + "X-INIT";

        String CONNECT = Prefix._EVENT + "X-CONNECT";
    }

    interface File {

        String UPLOAD = Prefix._EVENT + "X-UPLOAD";

        String DOWNLOAD = Prefix._EVENT + "X-DOWNLOAD";
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

        String IDENTIFIERS = Prefix._EVENT + "X-IDENTIFIER/LIST";
    }

    interface History {

        String HISTORIES = Prefix._EVENT + "X-ACTIVITY/HISTORIES";

        String HISTORY_ITEMS = Prefix._EVENT + "X-ACTIVITY/HISTORY-ITEMS";

        String HISTORY_BY_FIELDS = Prefix._EVENT + "X-ACTIVITY/HISTORY-BY-FIELDS";
    }
}
