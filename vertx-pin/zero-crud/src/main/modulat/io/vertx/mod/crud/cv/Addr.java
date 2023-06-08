package io.vertx.mod.crud.cv;

/**
 * Address for Event Bus
 */
interface Prefix {
    /*
     * Distinguish from original definition or user defined event.
     */
    String _EVENT = "Ἀτλαντὶς νῆσος://μονάδα μέτρησης/";
}

public interface Addr {

    interface Get {

        String BY_ID = Prefix._EVENT + "X-GET/ID";

        String BY_SIGMA = Prefix._EVENT + "X-GET/ALL";

        String COLUMN_FULL = Prefix._EVENT + "X-COLUMN/FULL";

        String COLUMN_MY = Prefix._EVENT + "X-COLUMN/MY";
    }

    interface Delete {

        String BY_ID = Prefix._EVENT + "X-DELETE/ID";

        String BATCH = Prefix._EVENT + "X-DELETE/BATCH";
    }

    interface File {

        String IMPORT = Prefix._EVENT + "X-FILE/IMPORT";

        String EXPORT = Prefix._EVENT + "X-FILE/EXPORT";
    }

    interface Post {

        String ADD = Prefix._EVENT + "X-ADD";

        String SEARCH = Prefix._EVENT + "X-SEARCH";

        String EXISTING = Prefix._EVENT + "X-EXISTING";

        String MISSING = Prefix._EVENT + "X-MISSING";
    }

    interface Put {

        String BY_ID = Prefix._EVENT + "X-PUT/ID";

        String BATCH = Prefix._EVENT + "X-PUT/BATCH";

        String COLUMN_MY = Prefix._EVENT + "X-COLUMN/MY/PUT";
    }
}
