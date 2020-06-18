package io.vertx.tp.rbac.cv;

/**
 * Address for Event Bus
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Ασφάλεια/";
}

public interface Addr {

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

    interface Authority{
        /*
         * Api Seeking
         */
        String ACTION_SEEK = Prefix._EVENT + "X-ACTION/SEEK";
    }

    interface Group {
        String GROUP_SIGMA = Prefix._EVENT + "S-GROUP/SIGMA";
    }

    interface Role {
        String ROLE_SIGMA = Prefix._EVENT + "S-ROLE/SIGMA";
    }
}
