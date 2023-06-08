package io.vertx.mod.ui.cv;

/**
 * Address for Event Bus
 */
interface Prefix {
    /*
     *
     */
    String _EVENT = "Ἀτλαντὶς νῆσος://Διασύνδεση χρήστη/";
}

public interface Addr {

    interface Page {

        String FETCH_AMP = Prefix._EVENT + "X-PAGE/AMP";
    }

    interface Control {

        String FETCH_BY_VISITOR = Prefix._EVENT + "X-VISITOR/BY/IDENTIFIER";

        String FETCH_BY_ID = Prefix._EVENT + "X-CONTROL/BY-ID";

        String FETCH_OP = Prefix._EVENT + "X-OP/BY-ID";

        String FETCH_FORM_BY_CODE = Prefix._EVENT + "X-FORM/BY-CODE";

        String FETCH_FORM_BY_IDENTIFIER = Prefix._EVENT + "X-FORM/BY-IDENTIFIER";

        String FETCH_LIST_BY_IDENTIFIER = Prefix._EVENT + "X-LIST/BY-IDENTIFIER";

        String FETCH_LIST_QR_BY_CODE = Prefix._EVENT + "X-LIST-QR/BY/CODE";

        String PUT_FORM_CASCADE = Prefix._EVENT + "X-FORM/PUT/CASCADE";

        String DELETE_FORM = Prefix._EVENT + "X-FORM/DELETE";
    }
}
